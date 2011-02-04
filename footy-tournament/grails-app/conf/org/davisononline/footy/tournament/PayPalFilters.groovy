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

                def entries = Entry.findAllByBuyerId(session['org.davisononline.footy.tournament.buyerId'])
                entries.each { e->
                    payment.addToPaymentItems(
                        new PaymentItem(
                            itemName: "${e.club.name} ${e.ageGroup} ${e.teamName}", 
                            itemNumber: "STBGFC Tournament",
                            amount: 28.00
                        )
                    )
                }
                payment.save(flush:true)
                params.transactionId = payment.transactionId

                entries.each { e-> 
                    e.payment = payment
                    e.save() 
                }

                return true
            }
        }

        buyFilter(controller:"paypal", action:"buy") {
            before = {
                // ensure we have at least one
                def e = Entry.findBySessionKey(params.buyerId)
                if (!e) 
                    throw new IllegalStateException("Cannot find entry for this payment!")
            }

            after = {
                log.debug("### after buy:")
                log.debug(params)
                def entries = Entry.findAllBySessionKey(params.buyerId)
                entries.each { e -> 
                    e.payment = request.payment
                    e.save()
                }
            }
        }

        paymentReceivedFilter(controller:'paypal', action:'(success|notify)') {
            after = {
                def payment = request.payment
                if(payment && payment.status == org.grails.paypal.Payment.COMPLETE) {
                    def entries = Entry.findAllByPayment(request.payment)
                    def toSend = []
                    entries.each { e ->
                        log.debug("Processed $e for payment")

                        if (!e.emailSent) {
                            // confirm email.. needs factoring out of the filter
                            def email = [
                                to:      ['darren@davisononline.org'], // s/be [e.email] but only send to me for now!, 
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
                            e.emailSent = true
                            toSend << email
                        }
                        e.save()
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
                def e = Entry.get(params.buyerId)
                if (e) {
                    // may want to just mark a status on it instead
                    log.debug("Deleting entry ${e}")
                    //e.delete()
                }
            }
        }
    }
}

