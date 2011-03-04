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

}
