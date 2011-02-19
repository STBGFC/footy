<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
        <title><g:message code="default.paymentdetails.label" default="Enter Payment Details" /></title>
    </head>
    <body>
        <div class="dialog">
            <g:form action="registerPlayer">
                <p>
                    The following player has been registered.  You can 
                    click the PayPal button in order to make payment now
                    either from a PayPal account or a credit card.
                </p>
        
                <div class="buttons">
                    <span class="button"><g:submitButton name="submit" class="save" value="${message(code: 'default.button.create.label', default: 'Continue')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
        