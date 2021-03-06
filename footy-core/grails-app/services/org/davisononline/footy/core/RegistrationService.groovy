package org.davisononline.footy.core
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.grails.paypal.Payment
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
     * send a token by email for registration renewals.
     */
    void sendTokenByEmail(String emailAddress, String token) {

        try {
            log.info "Sending token ${token} to ${emailAddress} for registration"
            mailService.sendMail {
                // ensure mail address override is set in dev/test in Config.groovy
                to      emailAddress
                subject 'Email Validation'
                body    ( view:'/email/core/validate',
                          model:[token:token, club: Club.homeClub])
            }
        }
        catch (Exception ex) {
            log.error "Unable to send email for validation during registration; $ex"
        }
    }

    /**
     * save domain objects for registration flow and return generated payment to
     * display on invoice
     * 
     * @param registrations
     * @return
     */
    Payment createPayment(registrations) {

        log.info "Creating payment for registration set ${registrations}"
        registrations.each { registration ->
            log.debug "Saving player ${registration.player} and registration ${registration}"
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
        log.debug "Created payment ${payment}"
        
        // and again to create items..
        registrations.sort{it.player.dateOfBirth}.each { registration ->
            def p = registration.player
            def regItem = new PaymentItem (
                    itemName: "${p} ${registration.tier}",
                    itemNumber: "${registration.id}",
                    amount: registration.tier.amount
            )
            log.debug "Creating registration item ${regItem}"
            
            if (
                p.sibling &&
                p.sibling.dateOfBirth <= p.dateOfBirth &&
                registration.tier.siblingDiscount != 0 &&
                p.sibling.currentRegistration.inDate()
            ) {
                log.debug "Adding sibling discount for ${p} with sibling ${p.sibling}"
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
            log.info "Sending email to ${buyer.email} with registration confirmation"
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

        log.info "Generating registration form for Team ${team}"
        try {
            def reporter = Class.forName("org.davisononline.footy.core.reports.${team.league.name}", true, getClass().getClassLoader()).newInstance()
            reporter.team = team
            reporter.out = out
            reporter.createPdf()
        }
        catch (Exception ex) {
            log.error "Unable to load league registration form class for ${team.league.name}"
        }

    }
}
