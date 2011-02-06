<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title><g:message code="confirm.entry.heading" default="Confirm Entry" /></title>
    </head>

    <body>
        <p>
            The following teams are ready to be entered.  Please choose whether to add more 
            teams or proceed to payment.
        </p>
        <g:form action="apply">
        <table><tbody>
        <g:each in="${entryInstance.teams}" var="team">
            <tr class="prop">
                <td class="name"><g:message code="entry.teamName.label" default="Team Name" /></td>
                <td class="value"><strong>${team.club} ${team} (${team.league} Div. ${team.division})</strong></td>
            </tr>   
        </g:each>       
        </tbody></table>
        <div class="buttons">
            <span class="button"><g:submitButton name="createMore" class="create" value="Add More Teams"></g:submitButton></span>
            <span class="button"><g:submitButton name="submit" class="save" value="Continue to Payment"></g:submitButton></span>
        </div>
        </g:form>
    </body>
</html>
