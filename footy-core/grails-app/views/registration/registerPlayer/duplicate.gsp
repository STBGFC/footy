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
                database.
            </p>
            <ul>
                <li>
                    If you are trying to pay online for a recently submitted registration, please refer to the email
                    you received at the time which has a link back to your invoice.
                </li>
                <li>
                    If you are trying to renew a registration, please await the automated email and follow
                    instructions for renewals.
                </li>
                <li>
                    If you think this is a mistake, or this is a first-time registration, please contact the person that
                    asked you to register and inform them of this message.
                </li>
            </ul>
        </div>
    </body>
</html>
        