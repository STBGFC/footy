<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
        <title><g:message code="default.guardiandetails.label" default="Enter Parent/Guardian Details" /></title>
    </head>
    <body>
        <div class="dialog">
            <g:form action="registerPlayer">
                <p>The player is a minor: please enter details of the parent or guardian in the fields below.</p>
        
                <div class="buttons">
                    <span class="button"><g:submitButton name="continue" class="save" value="${message(code: 'default.button.continue.label', default: 'Continue')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
        