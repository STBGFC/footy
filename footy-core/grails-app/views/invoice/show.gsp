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
                A copy of the invoice is shown below
            </p>
            <g:render template="/invoice" model="[controller:'invoice']" />
        </div>
    </body>
</html>
        