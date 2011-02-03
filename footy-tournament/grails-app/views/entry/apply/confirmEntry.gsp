<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title><g:message code="confirm.entry.heading" default="Confirm Entry" /></title>
    </head>

    <body>
        <div class="body">
        <h1><g:message code="confirm.entry.heading" default="Teams Entered" /></h1>

        <g:form action="apply">
        <div class="dialog">
        <p>
            The following team(s) are now added to your entry form:
        </p>
        <ul>
            <g:each var="team" in="${entryInstance.teams}">
            <li>${team.club.name} ${team}</li>
            </g:each>
        </ul>
        <p>
            <g:set var="numTeams" value="${entryInstance.teams.size()}"/>
            You have <strong>${numTeams}</strong> team${numTeams > 1 ? 's' : ''} @ &pound;${entryInstance.tournament.costPerTeam} each 
            (total: &pound;${numTeams*entryInstance.tournament.costPerTeam}).
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
