package org.davisonononline.footy.tournament

import org.grails.paypal.Payment
import org.grails.paypal.PaymentItem
import org.davisononline.footy.tournament.Entry
import org.davisononline.footy.core.utils.TemplateUtils
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class TournamentService {

    static transactional = true
    
    def mailService

    def fromEmail = ConfigurationHolder.config?.org?.davisononline?.footy?.tournament?.registration?.email
    def registrationEmailBody = ConfigurationHolder.config?.org?.davisononline?.footy?.tournament?.registration?.emailbody

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
            body    TemplateUtils.eval(registrationEmailBody, [entry: entry])
        }
    }

    /**
     * create and persist Payment for invoice based on tournament entry flow
     * 
     * @param entry
     * @return
     */
    Payment createPayment(Entry entry) {

        def payment = new Payment (
            buyerId: entry.contact.id,
            currency: Currency.getInstance("GBP"),
            transactionIdPrefix: "TRN"
        )
        entry.teams.each { t->
            payment.addToPaymentItems(
                new PaymentItem (
                    itemName: "${t.club.name} ${t}",
                    itemNumber: "${entry.tournament.name}",
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
