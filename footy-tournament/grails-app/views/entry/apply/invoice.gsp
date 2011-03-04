<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title><g:message code="enter.payment.heading" default="Enter Payment" /></title>
    </head>

    <body>
    <div class="list">
        <p>
            You are entering the following team(s) into <strong>${entryInstance.tournament.name}</strong>
        </p>
        <g:render template="/invoice" model="[controller:'entry']" plugin="footy-core" />
        <p>
            You will receive email confirmation as soon as payment clears (which is
            normally immediate in the case of PayPal)
        </p>
    </div>
    </body>
</html>
