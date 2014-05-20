<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
        <title><g:message code="org.davisononline.org.footy.core.registration.verifyfirsttime.label" default="Unknown Email" /></title>
    </head>
    <body>
        <h1>
            <g:message code="org.davisononline.org.footy.core.registration.verifyfirsttime.label" default="Unknown Email" />
        </h1>
        <div class="dialog">
            <g:form name="registration" action="register">
                <g:message code="org.davisononline.footy.core.registration.verifyfirsttime.text"
                    default="Verify first time registration"/>
                <div class="buttons flowcontrol">
                    <span class="button"><g:submitButton name="end" class="delete" value="${message(code: 'org.davisononline.footy.core.registration.button.alreadyregistered.label', default: 'I should already be in the system')}" /></span>
                    <span class="button"><g:submitButton name="continue" class="save" value="${message(code: 'org.davisononline.footy.core.registration.button.verifyfirsttime.label', default: 'Continue.. this is a FIRST TIME registration')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>

