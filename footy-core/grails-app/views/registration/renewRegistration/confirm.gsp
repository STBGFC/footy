<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
        <title><g:message code="org.davisononline.org.footy.core.registration.renewal.confirms.label" default="Confirm Re-registrations" /></title>
    </head>
    <body>
        <h1>
            <g:message code="org.davisononline.org.footy.core.registration.renewal.confirms.label" default="Confirm Re-registrations" />
        </h1>

        <div class="dialog">
            <g:form name="registration" action="renewRegistration">

                <ul>
                    <g:each in="${registeredPlayers}" var="p">
                    <li style="font-size:large">${p}</li>
                    </g:each>
                </ul>

                <p>
                    The above players are to be re-registered.  *** NATHAN's BLURB HERE ***
                </p>

                <div class="buttons flowcontrol">
                    <span class="button"><g:submitButton name="yes" class="save" value="${message(code: 'default.button.final.addmoreplayers.label', default: 'I accept, generate an invoice...')}" /></span>
                    <span class="button"><g:submitButton name="no" class="delete" value="${message(code: 'default.button.final.continue.label', default: 'I do NOT accept...')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
