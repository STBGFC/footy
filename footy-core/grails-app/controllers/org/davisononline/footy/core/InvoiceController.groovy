package org.davisononline.footy.core

import org.grails.paypal.Payment
import grails.plugins.springsecurity.Secured
import org.davisononline.footy.core.utils.PaymentUtils

/**
 * used for enabling an incomplete payment to be made again
 */
@Secured(['ROLE_OFFICER'])
class InvoiceController {

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 25, 100)
        params.order = params.order ?: "desc"
        params.sort = params.sort ?: "id"
        [paymentList: Payment.findAllByStatus(Payment.COMPLETE, params), paymentTotal: Payment.countByStatus(Payment.COMPLETE)]
    }

    def unpaid = {
        params.max = Math.min(params.max ? params.int('max') : 25, 100)
        params.order = params.order ?: "desc"
        params.sort = params.sort ?: "id"
        [paymentList: Payment.findAllByStatusInList([Payment.CANCELLED, Payment.PENDING, Payment.FAILED], params),
                paymentTotal: Payment.countByStatusInList([Payment.CANCELLED, Payment.PENDING, Payment.FAILED])]
    }

    def delete = {
        def payment = Payment.findByTransactionId(params?.id)
        if (!payment) {
            redirect view: '/404'
        }
        //TODO: should be atomic, move to service
        def person = Person.withCriteria {
            payments {
                eq "transactionId", payment.transactionId
            }
        }
        if (person) {
            person[0].removeFromPayments(payment)
        }
        payment.delete()
        flash.message = "Invoice and payment data deleted"
        redirect action: 'list'
    }

    @Secured(["permitAll"])
    def show = {
        def payment = Payment.findByTransactionId(params?.id)
        if (!payment) {
            redirect view: '/404'
        }
        [payment: payment, controller: params?.returnController ?: 'invoice']
    }

    @Secured(["permitAll"])
    def paypalSuccess = {
        render view: '/paypal/success', model:[payment: Payment.findByTransactionId(params.transactionId)]
    }

    @Secured(["permitAll"])
    def paypalCancel = {
        render view: '/paypal/cancel'
    }

    /**
     * render template to confirm manual payment amount
     */
    def paymentDialog = {
        render (template: 'paymentDialog', model: params, contentType: 'text/plain', plugin: 'footy-core')
    }

    /**
     * manually marks a registration payment as having been made (i.e. outside
     * of the PayPal/credit card route)
     */
    def paymentMade = {
        def payment = Payment.findByTransactionId(params.id)
        if (payment) {
            payment.status = Payment.COMPLETE
            PaymentUtils.adjustForManual(payment, params.amount.asType(BigDecimal))
            payment.save()
            flash.message = "Payment made for invoice ${params.id}"
        }
        else
            flash.message = "No such invoice found with number ${params.id}"

        redirect(action: "unpaid", params:params)
    }
}
