package org.davisononline.footy.core.utils

import grails.test.*
import org.grails.paypal.*

class PaymentUtilsTests extends GrailsUnitTestCase {

    void testTotalNoPayment() {
        assertEquals 0, PaymentUtils.calculateTotal(null)
        assertEquals 0, PaymentUtils.calculateTotal(new Payment())
    }

    void testNoTax() {
        mockDomain(Payment)
        def p = new Payment()
        p.addToPaymentItems (
                new PaymentItem(
                        amount: 10,
                        quantity: 1,
                        itemName: 'foo',
                        itemNumber: 123
                )
        )
        assertEquals 10, PaymentUtils.calculateTotal(p)
    }

    void testNoQuantity() {
        mockDomain(Payment)
        def p = new Payment()
        p.addToPaymentItems (
            new PaymentItem(
                amount: 10,
                itemName: 'foo',
                itemNumber: 123
                )
            )
        assertEquals 10, PaymentUtils.calculateTotal(p)
    }

    void testQuantity() {
        mockDomain(Payment)
        def p = new Payment()
        p.addToPaymentItems (
                new PaymentItem(
                        amount: 10,
                        quantity: 2,
                        itemName: 'foo',
                        itemNumber: 123
                )
        )
        assertEquals 20, PaymentUtils.calculateTotal(p)
    }

    void testTax() {
        mockDomain(Payment)
        def p = new Payment(tax: 0.1)
        p.addToPaymentItems (
                new PaymentItem(
                        amount: 10,
                        quantity: 2,
                        itemName: 'foo',
                        itemNumber: 123
                )
        )
        assertEquals 22, PaymentUtils.calculateTotal(p)
    }

    void testTaxRoundingUp() {
        mockDomain(Payment)
        def p = new Payment(tax: 0.1)
        p.addToPaymentItems (
                new PaymentItem(
                        amount: 10.55,
                        quantity: 1,
                        itemName: 'foo',
                        itemNumber: 123
                )
        )
        assertEquals 11.61, PaymentUtils.calculateTotal(p)
    }

    void testManualAdjustment() {
        mockDomain(Payment)
        def p = new Payment()
        p.addToPaymentItems (
                new PaymentItem(
                        amount: 10,
                        quantity: 1,
                        itemName: 'foo',
                        itemNumber: 123
                )
        )

        PaymentUtils.adjustForManual(p, 10)
        assertEquals 10, PaymentUtils.calculateTotal(p)

        PaymentUtils.adjustForManual(p, 5)
        assertEquals 5, PaymentUtils.calculateTotal(p)
        assertEquals 10, p.paymentItems[0].amount
        assertEquals 5, p.paymentItems[0].discountAmount

        p.addToPaymentItems (
                new PaymentItem(
                        amount: 10,
                        quantity: 1,
                        itemName: 'bar',
                        itemNumber: 321
                )
        )
        assertEquals 15, PaymentUtils.calculateTotal(p)
        assertEquals 2, p.paymentItems.size()

        PaymentUtils.adjustForManual(p, 10)
        assertEquals 10, PaymentUtils.calculateTotal(p)
        assertEquals 10, p.paymentItems[0].amount
        assertEquals 10, p.paymentItems[0].discountAmount
        assertEquals 10, p.paymentItems[1].amount
    }

    void testManualAdjustmentOverPaid() {
        mockDomain(Payment)
        def p = new Payment()
        p.addToPaymentItems (
                new PaymentItem(
                        amount: 10,
                        quantity: 1,
                        itemName: 'foo',
                        itemNumber: 123
                )
        )

        assertEquals 10, PaymentUtils.calculateTotal(p)
        assertEquals 1, p.paymentItems.size()
        
        PaymentUtils.adjustForManual(p, 20)
        assertEquals 20, PaymentUtils.calculateTotal(p)
        assertEquals 2, p.paymentItems.size()
        assertEquals 10, p.paymentItems[1].amount
    }

}
