package org.davisononline.footy.core

import org.grails.paypal.Payment

/**
 * used for enabling an incomplete payment to be made again
 */
class InvoiceController {

    def show = {
        def payment = Payment.findByTransactionId(params?.id)
        if (!payment) {
            redirect view: '/404'
        }
        [payment: payment, controller: params?.returnController ?: 'invoice']
    }

    def paypalSuccess = {
        render view: '/paypal/success', model:[payment: Payment.findByTransactionId(params.transactionId)]
    }

    def paypalCancel = {
        render view: '/paypal/cancel'
    }
}
