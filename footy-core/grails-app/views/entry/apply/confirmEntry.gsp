<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title><g:message code="org.davisononline.footy.tournament.views.entry.apply.confirmentry.title" default="Confirm Entry" /></title>
    </head>

    <body>
        <h1><g:message code="org.davisononline.footy.tournament.views.entry.apply.confirmentry.title" default="Confirm Entry" /></h1>
        <div class="dialog">
        <p>
            The following teams are ready to be entered.  Please choose whether to add more 
            teams or proceed to payment. Once you move to the payment screen, you will no
            longer be able to go back and make changes.
        </p>
        <g:form action="apply">
        <ul id="confirmEntryList" class="nice-list">
        <g:each in="${entryInstance.teams}" var="team">
            <li class="teamNotRegistered">
                <g:checkBox name="teamIds" value="${team.id}" checked="${false}"/> <strong>${team.club} ${team}</strong> <g:if test="${team.division}">(${team.division})</g:if>
            </li>
        </g:each>       
        </ul>
        <div class="buttons">
            <span class="button"><g:submitButton name="removeTeams" class="delete" value="Remove Checked Teams"></g:submitButton></span>
            <span class="button"><g:submitButton name="createMore" class="create" value="Add More Teams"></g:submitButton></span>
            <span class="button"><g:submitButton name="submit" class="save" value="Continue to Payment"></g:submitButton></span>
        </div>
        </g:form>
        </div>
    </body>
</html>
