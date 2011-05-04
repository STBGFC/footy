<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title><g:message code="org.davisononline.footy.core.duplicateregistration.title" default="Already Registered" /></title>
    </head>
    <body>
        <div class="dialog">
            <p>
                A player named <strong>${player}</strong> with the same date of birth is already registered in the
                database.  If you are trying to renew a registration, please await the automated email and follow
                instructions for renewals.
            </p>
            <p>
                If you think this is a mistake, or this is a first-time registration, please contact the person that
                asked you to register and inform them of this message.
            </p>
        </div>
    </body>
</html>
        