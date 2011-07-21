<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title><g:message code="org.davisononline.footy.core.duplicateregistration.title" default="Already Registered" /></title>
    </head>
    <body>
        <div class="dialog">
            <div class="message">
                A player named <strong>${player}</strong> with the same date of birth is already registered in the
                database.  You must ensure that you are NOT trying to register a player whose details have already
                been entered through this process.
            </div>
            <p>
                Please read the following options carefully.
            </p>
            <ol>
                <li>
                    If you are trying to pay online for a recently submitted registration,
                    <g:if test="${payment}">
                    please check <g:link controller="invoice" action="show" id="${payment.transactionId}">this invoice</g:link>
                    to see if it's yours and click the "checkout" button if so.
                    </g:if>
                    <g:else>
                    please refer to the email you received at the time which has a link back to your invoice.
                    </g:else>
                </li>
                <li>
                    If you are trying to renew a registration, please await the automated email and follow
                    instructions for renewals.
                </li>
                <li>
                    If you think this is a mistake, or this is a first-time registration, please contact the person that
                    asked you to register and inform them of this message.
                </li>
            </ol>
        </div>
    </body>
</html>
        