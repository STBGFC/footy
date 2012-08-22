<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
        <title><g:message code="org.davisononline.org.footy.core.registration.renewal.confirms.label" default="Confirm Registrations" /></title>
    </head>
    <body>
        <h3>
           <g:message
                   code="org.davisononline.footy.core.registration.renew.addmore.text"
                   default="The following player(s) are selected for registration renewal.  Please choose whether to re-register more or continue to payment"/>
        </h3>
        <div class="dialog">
            <g:form name="registration" action="renewRegistration">

                <ul>
                    <g:each in="${registeredPlayers}" var="p">
                    <li>${p}</li>
                    </g:each>
                </ul>

                <div class="buttons flowcontrol">
                    <span class="button"><g:submitButton name="yes" class="edit" value="${message(code: 'default.button.final.addmoreplayers.label', default: 'Register More Players')}" /></span>
                    <span class="button"><g:submitButton name="no" class="save" value="${message(code: 'default.button.final.continue.label', default: 'Finish and Pay...')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
