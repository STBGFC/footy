package org.davisononline.footy.tournament

import org.davisononline.footy.core.*
import org.grails.paypal.*

/**
 * @author darren
 */
class PayPalFilters {

    EmailService emailService

    def filters = {

        uploadCartFilter(controller:"paypal", action:"uploadCart") {
            before = {
                def payment = new Payment(
                    buyerId: session['org.davisononline.footy.tournament.buyerId'],
                    currency: Currency.getInstance("GBP")
                )

                def entry = Entry.get(session['org.davisononline.footy.tournament.buyerId'])
                entry.teams.each { t->
                    payment.addToPaymentItems(
                        new PaymentItem(
                            itemName: "${t.club.name} U${t.ageBand} ${t.name}", 
                            itemNumber: "${entry.tournament.name} Tournament",
                            amount: entry.tournament.costPerTeam
                        )
                    )
                }
                payment.save(flush:true)
                params.transactionId = payment.transactionId

                entry.payment = payment
                entry.save() 

                return true
            }
        }

        paymentReceivedFilter(controller:'paypal', action:'(success|notify)') {
            after = {
                def payment = request.payment
                if(payment && payment.status == org.grails.paypal.Payment.COMPLETE) {
                    def entry = Entry.findByPayment(request.payment)
                    def toSend = []
                    log.debug("Processed $e for payment")

                    if (!entry.emailConfirmationSent) {
                        // TODO: confirm email.. needs factoring out of the filter
                        def email = [
                            // TODO: change to [entry.contact.email]. Only sending to me for now!,
                            to:      ['darren@davisononline.org'],  
                            subject: "Entry Confirmation", 
                            text:    """(Automatic email, please do not reply to this address)

Dear ${e.contactName},

Thank you for your entry to the STBGFC tournament.  This email confirms that
the ${e.club.name} "${e.teamName}" are entered into the ${e.ageGroup}
competition and that payment has been received.

We'll see you there!
STBGFC Tournament Committee.
"""
                        ]
                        entry.emailConfirmationSent = true
                        toSend << email
                        entry.save()
                    }
                    emailService.sendEmails(toSend)
                    request.entries = entries
                }
                
                log.debug("### after IPN:")
                log.debug(params)
                log.debug(request.payment)
            }
        }

        transactionCancelledFilter(controller:'paypal', action:'cancel') {
            after = {
                def e = Entry.get(session['org.davisononline.footy.tournament.buyerId'])
                if (e) {
                    log.debug("Transaction cancelled on entry ${e}")
                    e.payment?.status = Payment.CANCELLED
                    e.payment?.save(flush:true)
                    e.save()
                }
            }
        }
    }
}

