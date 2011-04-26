package org.davisonononline.footy.tournament

import org.grails.paypal.Payment
import org.grails.paypal.PaymentItem
import org.davisononline.footy.tournament.Entry

class TournamentService {

    static transactional = true
    
    def mailService

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
            from    "tournament@stbgfc.co.uk"
            subject "Tournament Entry Confirmation"
            body    """(Automatic email, please do not reply to this address)

Hi ${entry.contact.knownAsName ?: entry.contact.givenName},

Thank you for your entry to the STBGFC tournament.  This email confirms that
the the following teams are entered into the ${entry.tournament.name} 
competition, and that payment has been received.

${entry.teams.join('\n')}

We'll see you there!
STBGFC Tournament Committee.
"""
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
