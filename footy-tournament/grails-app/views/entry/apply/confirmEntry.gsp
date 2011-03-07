<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title><g:message code="confirm.entry.heading" default="Confirm Entry" /></title>
    </head>

    <body>
        <div class="dialog">
        <p>
            The following teams are ready to be entered.  Please choose whether to add more 
            teams or proceed to payment. Once you move to the payment screen, you will no
            longer be able to go back and make changes.
        </p>
        <g:form action="apply">
        <ul id="confirmEntryList">
        <g:each in="${entryInstance.teams}" var="team">
            <li>${team.club} ${team} (${team.league} Div. ${team.division})</li> 
        </g:each>       
        </ul>
        <div class="buttons">
            <span class="button"><g:submitButton name="createMore" class="create" value="Add More Teams"></g:submitButton></span>
            <span class="button"><g:submitButton name="submit" class="save" value="Continue to Payment"></g:submitButton></span>
        </div>
        </g:form>
        </div>
    </body>
</html>
