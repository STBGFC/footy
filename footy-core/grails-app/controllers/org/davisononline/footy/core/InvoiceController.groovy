package org.davisononline.footy.core

import org.grails.paypal.Payment
import grails.plugins.springsecurity.Secured
import org.davisononline.footy.core.utils.PaymentUtils
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * used for enabling an incomplete payment to be made again
 */
@Secured(['ROLE_OFFICER'])
class InvoiceController {

    def exportService

    def springSecurityService

    def list = {
        if(params?.format && params.format != "html"){
            // includes unpaid
            List fields = [
                    "transactionId", "status", "paypalTransactionId", "paymentMethod", "total"
            ]
            Map labels = [
                    "transactionId": "Number",
                    "status": "Payment Status",
                    "paypalTransactionId": "PayPal Transaction ID",
                    "paymentMethod": "Payment Method",
                    "total": "Amount"
            ]

            // Formatter closure
            def calculated = { payment, value ->
                PaymentUtils.calculateTotal(payment)
            }
            def method = { payment, value ->
                if (payment.paypalTransactionId) "PayPal"
                else if (payment.status == Payment.COMPLETE) "Cash/Chq/CC"
                else ""
            }
            Map formatters = [
                    total: calculated,
                    paymentMethod: method
            ]
            Map parameters = [title: "All Invoices"]

            response.contentType = ConfigurationHolder.config.grails.mime.types[params.format]
            response.setHeader(
                "Content-disposition",
                "attachment; filename=${URLEncoder.encode('invoice-list','UTF-8')}.${params.extension}"
            )
            exportService.export(
                params.format,
                response.outputStream,
                Payment.list([sort:'transactionId', order: 'desc']),
                fields,
                labels,
                formatters,
                parameters
            )
        }
        else {
            // standard, paid invoices
            params.max = Math.min(params.max ? params.int('max') : 25, 100)
            params.order = params.order ?: "desc"
            params.sort = params.sort ?: "id"
            [paymentList: Payment.findAllByStatus(Payment.COMPLETE, params), paymentTotal: Payment.countByStatus(Payment.COMPLETE)]
        }
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
        def username =
            session[UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY] ?:
            springSecurityService.authentication.name
        def payment = Payment.findByTransactionId(params.id)
        if (payment) {
            payment.status = Payment.COMPLETE
            PaymentUtils.adjustForManual(payment, params.amount.asType(BigDecimal), "${params.notes} (${params.refund ? 'add' : 'refund'}ed by user: ${username})")
            payment.save()
            flash.message = "${params.refund ? 'Refund' : 'Payment'} made for invoice ${params.id}"
        }
        else
            flash.message = "No such invoice found with number ${params.id}"

        redirect(action: "show", id:payment.transactionId, params:params)
    }
}
