package org.davisononline.footy.tournament.paypal

import org.davisononline.footy.tournament.*
import org.davisononline.footy.core.*
import org.grails.paypal.*

 
/**
 * @author darren
 */
class PayPalFilters {

    def tournamentService

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
                entry.save(flush:true) 

                return true
            }
        }

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

