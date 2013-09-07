package org.davisononline.footy.core

import org.grails.paypal.Payment
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.grails.paypal.PaymentItem
import org.hibernate.jdbc.Work
import java.sql.Connection

class RegistrationService {

    /* use the hibernate session factory directly to get around a bug (see later comments where used) */
    def sessionFactory

    def mailService

    static transactional = true

    def CFG = ConfigurationHolder.config?.org?.davisononline?.footy?.core?.registration

    def fromEmail = CFG?.email

    def currencyCode = CFG?.currency ?: "GBP"
    

    /**
     * save domain objects for registration flow and return generated payment to
     * display on invoice
     * 
     * @param registrations
     * @return
     */
    Payment createPayment(registrations) {

        registrations.each { registration ->
            registration.player.save()
            registration.save()
        }

        def p0 = registrations[0].player
        def buyer = p0.guardian  ?: p0.person

        def payment = new Payment (
            buyerId: buyer.id,
            transactionIdPrefix: "REG",
            currency: Currency.getInstance(currencyCode)
        )
        
        // and again to create items..
        registrations.each { registration ->
            def p = registration.player
            def regItem = new PaymentItem (
                    itemName: "${p} ${registration.tier}",
                    itemNumber: "${registration.id}",
                    amount: registration.tier.amount
            )
            if (p.sibling && p.sibling.dateOfBirth < p.dateOfBirth && registration.tier.siblingDiscount != 0) {
                regItem.discountAmount = registration.tier.siblingDiscount
            }
            payment.addToPaymentItems(regItem)
        }

        payment.save(flush:true)

        /*
         * create the join manually because trying to do;
         *     player.guardian.addToPayments(payment)
         * causes an NPE when the tx commits.  see http://jira.grails.org/browse/GRAILS-7471
         */
        sessionFactory.getCurrentSession().doWork(
                new Work() {
                    void execute(Connection conn) {
                        def stmt = conn.createStatement()
                        stmt.execute("INSERT INTO person_payment(person_payments_id, payment_id) VALUES (${buyer.id}, ${payment.id})")
                        stmt.close()
                    }
                }
            )


        try {
            mailService.sendMail {
                // ensure mail address override is set in dev/test in Config.groovy
                to      buyer.email
                from    fromEmail
                subject "Registration Confirmation"
                body    (view: '/email/core/registrationComplete',
                         model: [buyer: buyer, registrations:registrations, payment:payment, homeClub: Club.homeClub])
            }
        }
        catch (Exception ex) {
            log.warn "Unable to send email after registration; $ex"
        }

        // return generated payment
        payment

    }

    /**
     * send a registration form PDF to the output stream (out) based on the
     * team and template supplied.
     *
     * EBYFL specific
     */
    void generateRegistrationForm(team, out) {

        def reporter

        // GRrrrrr.. WTF does Class.forName fail??
        switch (team.league.name) {
            case "NEHYL":
                reporter = org.davisononline.footy.core.reports.NEHYL.newInstance()
                break

            default:
                reporter = org.davisononline.footy.core.reports.EBYFL.newInstance()
                break
        }

        if (!reporter) {
            log.error "Unable to find report class for league name ${team?.league?.name}"
        }
        else {
            reporter.team = team
            reporter.out = out
            reporter.createPdf()
        }
    }
}
