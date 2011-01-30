<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        
        <title><g:message code="confirm.entry.heading" default="Confirm Entry" /></title>
    </head>

    <body>
        <div class="body">
        <h1><g:message code="confirm.entry.heading" default="Confirm Entry" /></h1>

        <g:form action="apply">
        <div class="dialog">
        <p>
            Entry details for <strong>${entryInstance.ageGroup} ${entryInstance.teamName}</strong>
            completed OK.
        </p>
        <p>
            Please choose whether to add more teams or proceed to payment.
        </p>
        </div>
        <div class="buttons">
            <span class="button"><g:submitButton name="createMore" class="create" value="Add More Teams.."></g:submitButton></span>
            <span class="button"><g:submitButton name="submit" class="save" value="Continue to Payment.."></g:submitButton></span>
        </div>
        </g:form>
        </div>
    </body>
</html>

