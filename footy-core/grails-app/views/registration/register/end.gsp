<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
        <title><g:message code="org.davisononline.org.footy.core.registration.end.label" default="Registration Complete" /></title>
    </head>
    <body>
        <h1>
            <g:message code="org.davisononline.org.footy.core.registration.end.label" default="Registration Complete" />
        </h1>
        <div class="dialog">
            <p>
                ${endMessage}
            </p>
        </div>
    </body>
</html>
