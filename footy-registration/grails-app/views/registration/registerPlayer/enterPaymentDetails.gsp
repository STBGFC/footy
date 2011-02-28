<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
        <title><g:message code="default.paymentdetails.label" default="Enter Payment Details" /></title>
    </head>
    <body>
        <div class="dialog">
            <p>
                <strong>${player}</strong> has been registered.  You can
                click the PayPal button in order to make payment now
                either from a PayPal account or a credit card.
            </p>
            <p>
                <strong>Total: <g:formatNumber number="${registrationCost}" type="currency" currencyCode="GBP" /></strong>
            </p>
        </div>

        <g:form
                controller="paypal"
                action="uploadCart"
                params="[
                    transactionId: payment.transactionId,
                    returnController: 'registration',
                    returnAction: 'paypalSuccess',
                    cancelController: 'registration',
                    cancelAction: 'paypalCancel'
                ]">
        <input type="image" class="paypal"
            src="https://www.paypalobjects.com/WEBSCR-640-20110124-1/en_US/i/btn/btn_xpressCheckout.gif"
            alt="PayPal - The safer, easier way to pay"/>
        <img alt="" border="0" src="https://www.paypal.com/en_US/i/scr/pixel.gif" width="1" height="1"/>
        </g:form>

    </body>
</html>
        