package org.davisononline.footy.tournament.paypal

import org.davisononline.footy.tournament.*
import org.grails.paypal.*

 
/**
 * @author darren
 */
class PayPalFilters {

    def tournamentService

    def filters = {

        paymentReceivedFilter(controller:'paypal', action:'(success|notify)') {
            after = {
                def payment = request.payment
                if(payment && payment.status == org.grails.paypal.Payment.COMPLETE) {
                    def entry = Entry.findByPayment(request.payment)
                    log.debug("Processed ${entry} for payment")

                    if (!entry.emailConfirmationSent) {
                        tournamentService.sendConfirmEmail(entry)
                        entry.emailConfirmationSent = true
                        entry.save()
                    }

                    request.entry = entry
                }
                
                log.debug("### after IPN:")
                log.debug(params)
                log.debug(request.payment)
            }
        }

        transactionCancelledFilter(controller:'paypal', action:'cancel') {
            after = {
                request.payment?.status = Payment.CANCELLED
                request.payment?.save(flush:true)
            }
        }
    }
}

