<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
        <title><g:message code="org.davisononline.org.footy.core.registration.renewal.end.label" default="Renewal Ended" /></title>
    </head>
    <body>
        <h3>
            ${endMessage}
        </h3>
    </body>
</html>
