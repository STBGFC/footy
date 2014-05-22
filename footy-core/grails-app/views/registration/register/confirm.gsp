<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
        <title><g:message code="org.davisononline.org.footy.core.registration.renewal.confirms.label" default="Confirm Registrations" /></title>
    </head>
    <body>
        <h1>
            <g:message code="org.davisononline.org.footy.core.registration.renewal.confirms.label" default="Confirm Registrations" />
        </h1>

        <div class="dialog">
            <g:form name="registration" action="register">

                <p>
                    The players below are to be registered.
                </p>

                <ul>
                    <g:each in="${registeredPlayers}" var="p">
                    <li style="font-size:large">${p}</li>
                    </g:each>
                </ul>

                <g:message code="org.davisononline.footy.registration.views.registration.conditions" default="Terms and Conditions not specified"/>

                <div class="buttons flowcontrol">
                    <span class="button"><g:submitButton name="yes" class="save" value="${message(code: 'default.button.final.addmoreplayers.label', default: 'I accept, generate an invoice...')}" /></span>
                    <span class="button"><g:submitButton name="no" class="delete" value="${message(code: 'default.button.final.continue.label', default: 'I do NOT accept...')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
