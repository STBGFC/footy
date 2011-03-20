<%@ page import="org.grails.paypal.Payment; org.davisononline.footy.core.utils.PaymentUtils" %>

        <table id="invoice">
            <tbody>
            <tr class="invoiceHeader">
                <th>item #</th>
                <th>item name/description</th>
                <th class="amount">amount</th>
            </tr>
            <g:each in="${payment.paymentItems}" var="item">
            <tr class="invoiceBody">
                <td>${item.itemNumber}</td>
                <td>${item.itemName}</td>
                <td class="amount">
                    ${item.quantity} x <g:formatNumber number="${item.amount}" type="currency" currencyCode="${payment.currency}" />
                </td>
            </tr>
            <g:if test="${item.discountAmount > 0}">
            <tr class="invoiceBody">
                <td>&nbsp;</td>
                <td>${item.itemName} Discount</td>
                <td class="amount">
                    <g:formatNumber number="${-item.discountAmount}" type="currency" currencyCode="${payment.currency}" />
                </td>
            </tr>
            </g:if>
            </g:each>
            <tr class="invoiceBody">
                <td class="spacer">&nbsp;</td>
                <td class="spacer">&nbsp;</td>
                <td class="spacer">&nbsp;</td>
            </tr>
            <tr class="invoiceTotal">
                <td id="invisible">&nbsp;</td>
                <td class="amount">
                    <g:if test="${payment.tax > 0}">Tax (${payment.tax * 100}%)<br/></g:if>
                    Total
                </td>
                <td class="amount">
                    <g:if test="${payment.tax > 0}"><g:formatNumber number="${PaymentUtils.calculateTax(payment)}" type="currency" currencyCode="${payment.currency}" /><br/></g:if>
                    <g:formatNumber number="${PaymentUtils.calculateTotal(payment)}" type="currency" currencyCode="${payment.currency}" />
                </td>
            </tr>
            </tbody>
        </table>
        <g:if test="${payment.status != Payment.COMPLETE}">
        <div id="checkout">
            <g:form
                controller="paypal"
                action="uploadCart"
                params="[
                    transactionId:payment.transactionId,
                    returnController: controller,
                    returnAction: 'paypalSuccess',
                    cancelController: controller,
                    cancelAction: 'paypalCancel'
                ]">
            <img alt="" title="You can pay via credit card without requiring a PayPal account - click the 'Checkout' button"
                border="0"
                src="${resource(dir:'images', file:'paypalcards.gif', plugin: 'footy-core')}"/>
            <input type="image" class="paypal"
                src="${resource(dir:'images', file:'paypalcheckout.gif', plugin: 'footy-core')}"
                alt="Click to pay via PayPal - the safer, easier way to pay"/>
            </g:form>
            <p>
                <em>
                <g:message
                        code="org.davisononline.footy.core.invoicefooter.text"
                        default="You don't need a PayPal account to pay!  Once you get to the PayPal site, you can simply enter credit card details without needing to register or login"/>
                </em>
            </p>
        </div>
        </g:if>
        <g:else>
            <p>
                <g:message code="org.davisononline.footy.core.invoicepaid" default="This invoice has already been paid.  Thank you."/>
            </p>
        </g:else>