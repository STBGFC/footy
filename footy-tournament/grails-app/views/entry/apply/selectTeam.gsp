<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title><g:message code="select.club.heading" default="Register Teams" /></title>
    </head>

    <body>
        <div class="dialog">
        <g:form action="apply">
        <p>
            The following teams are already in our database for your club.  If
            you wish to register one or more of them for this tournament, please
            check the box next to the team name.  Teams in grey, without a check
            box have already been registered.
        </p>
        <p>
            If you need to add a team that's not currently listed, please click
            the "Add New Team.." button below, however you will need to select
            any teams below FIRST.
        </p>
        <ul id="teamSelect">
        <g:each in="${teamList}" var="team">
            <g:if test="${allEntries?.flatten().contains(team)}">
            <li class="teamRegistered">
                ${team}
            </li>
            </g:if>
            <g:else>
            <li class="teamNotRegistered">
                <g:checkBox name="teamIds" value="${team.id}" checked="${false}"/> ${team}
            </li>
            </g:else>
        </g:each>
        </ul>
        
        <div class="buttons">
            <span class="button"><g:submitButton name="selected" class="save" value="Register Checked Teams.."></g:submitButton></span>
            <span class="button"><g:submitButton name="createNew" class="create" value="Add New Team.."></g:submitButton></span>
        </div>
        </g:form>
        </div>
    </body>
</html>

