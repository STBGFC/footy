<%@ page import="org.davisononline.footy.core.Club; org.grails.paypal.Payment; org.davisononline.footy.core.utils.PaymentUtils" %>
        <p>
            <g:message code="org.davisononline.footy.core.invoicenumber.label" default="Invoice Number: "/><strong>${payment.transactionId}</strong>
            <g:if test="${payment.invoiceDate}">
            <br/><g:message code="org.davisononline.footy.core.invoicenumber.datelabel" default="Invoice Date: "/><strong><g:formatDate date="${payment.invoiceDate}" format="dd MMM yyyy"/></strong>
            </g:if>
        </p>
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
            <input type="image" class="paypal"
                src="${resource(dir:'images', file:'paypalcards.gif', plugin: 'footy-core')}"
                alt="Click to pay via PayPal or Credit Card"/>
            <input type="image" class="paypal"
                src="${resource(dir:'images', file:'paypalcheckout.gif', plugin: 'footy-core')}"
                alt="Click to pay via PayPal or Credit Card"/>
            </g:form>
        </div>
        <div class="alert"><g:message code="org.davisononline.footy.core.invoice.due.text" default="*** Invoice is now due for payment ***"/></div>
        <h2>Pay Online</h2>
        <p>
            <g:message
                    code="org.davisononline.footy.core.invoice.payonline.text"
                    default="You don't NEED a PayPal account to pay online!  Click the 'Checkout with PayPal' button above and you can either use your PayPal account OR enter credit card details without needing to register or login"/>

        </p>
        <h2>Pay Offline</h2>
        <p>
            <g:message
                code="org.davisononline.footy.core.invoice.payoffline.text"
                default="Our strong preference is that you pay online as described above using a credit card or PayPal account.  This makes it much easier for us.  However,if you prefer not to, then you can pay cash or send a cheque (with the INVOICE NUMBER written on the back) payable to "/>
            <strong><g:message
                code="org.davisononline.footy.core.invoice.paycheque.text"
                default="STBGFC"/></strong>
        </p>
        <p>
            ${Club.homeClub.treasurer}
            ${Club.homeClub.treasurer?.address}
        </p>
        </g:if>
        <g:else>
        <p>
            <g:message code="org.davisononline.footy.core.invoicepaid" default="This invoice has already been paid.  Thank you."/>
            <g:if test="${payment.paymentDate}"> (<g:formatDate date="${payment.paymentDate}" format="dd/MM/yyyy"/>)</g:if>
        </p>
        </g:else>