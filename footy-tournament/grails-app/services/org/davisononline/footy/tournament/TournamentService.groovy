package org.davisononline.footy.tournament

import org.grails.paypal.Payment
import org.grails.paypal.PaymentItem
import org.davisononline.footy.tournament.Entry
import org.codehaus.groovy.grails.commons.ConfigurationHolder


class TournamentService {

    static transactional = true
    
    def mailService

    def fromEmail = ConfigurationHolder.config?.org?.davisononline?.footy?.tournament?.registration?.email


    /**
     * send confirmation email after a successful payment has been made
     *
     * @param entry
     * @return
     */
    def sendConfirmEmail(entry) {
        
        mailService.sendMail {
            // ensure mail address override is set in dev/test in Config.groovy
            to      entry.contact.email
            from    fromEmail
            subject "Tournament Entry Confirmation"
            body    (view: '/email/tournament/registrationComplete',
                     model: [entry: entry])
        }
    }

    /**
     * create and persist Payment for invoice based on tournament entry flow
     * 
     * @param entry
     * @return
     */
    Payment createPayment(Entry entry) {

        if (!entry.contact.id)
            entry.contact.save()

        def payment = new Payment (
            buyerId: entry.contact.id,
            currency: Currency.getInstance("GBP"),
            transactionIdPrefix: "TRN"
        )
        entry.teams.each { t->
            if (!t.club.id) {
                t.club.secretary.save()
                t.club.save()
            }
            if (!t.manager.id)
                t.manager.save()
            
            t.save()
            payment.addToPaymentItems(
                new PaymentItem (
                    itemName: "${t.club.name} ${t} entry to ${entry.tournament.name}",
                    itemNumber: "${entry.tournament.id}-${t.id}",
                    amount: entry.tournament.costPerTeam
                )
            )
        }
        payment.save()
        entry.payment = payment
        entry.save(flush:true)
        payment
    }
}
