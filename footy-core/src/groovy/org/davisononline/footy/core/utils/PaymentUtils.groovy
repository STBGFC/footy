package org.davisononline.footy.core.utils

import org.grails.paypal.PaymentItem

/**
 */
class PaymentUtils {

    /**
     * WTF does payment not do this itself?
     *
     * @param Payment
     * @return
     */

    static calculateSubTotal(payment) {
        if (!payment?.paymentItems || payment.paymentItems.size() == 0)
            return 0

        // sub total items
        def tot = 0
        payment.paymentItems.each { item ->
            tot += ((item.amount * item.quantity) - item.discountAmount ?: 0)
        }
        tot
    }

    static calculateTax(payment) {
        if (!payment) return 0
        def act = calculateSubTotal(payment) * (payment.tax)
        Math.round(act * 100) / 100
    }

    static calculateTotal(payment) {
        if (!payment) return 0
        def act = calculateSubTotal(payment) * (payment.tax + 1)
        Math.round(act * 100) / 100
    }

    /**
     * if a manual payment is made for an amount other than the actual
     * total, the invoice items are fudged to make up this new total.
     */
    static adjustForManual(payment, amount, notes) {

        def diff = amount - calculateTotal(payment)

        def defaultNotes = (diff < 0 ? 'manual discount' : 'manual overpayment')

        if (diff != 0) {
            payment.addToPaymentItems(
                new PaymentItem(
                        amount: diff,
                        quantity: 1,
                        itemName: notes ?: defaultNotes,
                        itemNumber: 0
                )
            )
            payment.save()
        }
    }

}
