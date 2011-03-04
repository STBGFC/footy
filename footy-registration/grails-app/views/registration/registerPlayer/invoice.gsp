<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
        <title><g:message code="default.paymentdetails.label" default="Payment" /></title>
    </head>
    <body>
        <div class="list">
            <p>
                <strong>${player}</strong> has been registered.  You can
                click the PayPal button in order to make payment now
                either from a PayPal account or a credit card.
            </p>
            <g:render template="/invoice" model="[controller:'registration']" plugin="footy-core" />
        </div>
    </body>
</html>
        