package org.davisononline.footy.core

import org.grails.paypal.Payment
import grails.plugins.springsecurity.Secured

/**
 * used for enabling an incomplete payment to be made again
 */
class InvoiceController {

    @Secured(['ROLE_CLUB_ADMIN'])
    def list = {
        params.max = Math.min(params.max ? params.int('max') : 25, 100)
        params.order = params.order ?: "desc"
        params.sort = params.sort ?: "id"
        [paymentList: Payment.findAllByPaypalTransactionIdIsNotNull(params), paymentTotal: Payment.countByPaypalTransactionIdIsNotNull()]
    }

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
