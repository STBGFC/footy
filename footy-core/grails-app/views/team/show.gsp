
<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>${teamInstance}</title>
    </head>
    <body>

        <div id="homemain">
            <h2>Team Info</h2>

            <sec:ifAnyGranted roles="ROLE_CLUB_ADMIN,ROLE_MANAGER">
            <p>
                <strong>Manager:</strong>
                <g:link controller="person" action="edit" id="${teamInstance.manager.id}">${teamInstance.manager}</g:link>
            </p>
            <p>
                <strong>Coaches:</strong>
                <g:each in="${teamInstance.coaches}" var="c"><g:link controller="person" action="edit" id="${c.id}">${c}</g:link>, </g:each>
            </p>
            <p>
                <strong>Players:</strong>
                <g:each in="${players}" var="p"><g:link controller="player" action="edit" id="${p.id}">${p}</g:link>, </g:each>
            </p>
            <div class="nav">
                <span class="menuButton"><g:link class="edit" action="edit" id="${teamInstance.id}">edit this team</g:link></span>
            </div>
            </sec:ifAnyGranted>

            <sec:ifNotGranted roles="ROLE_CLUB_ADMIN,ROLE_MANAGER">
            <p>
                <strong>Manager:</strong> ${teamInstance.manager}
            </p>
            <p>
                <strong>Coaches:</strong> ${teamInstance.coaches.join(", ")}
            </p>
            <p>
                <strong>Players:</strong> ${players.join(", ")}
            </p>
            </sec:ifNotGranted>
            
        </div>


        <div id="newspanel">
            <div class="newsbox">
                <h2>${teamInstance} Upcoming Events</h2>
                <ul>
                    <li>none found</li>
                </ul>
            </div>
            <div class="newsbox">
                <h2>All U${teamInstance.ageBand} Teams</h2>
                <ul>
                    <g:each in="${Team.findAllByAgeBand(teamInstance.ageBand)}" var="t">
                    <g:if test="${t != teamInstance}">
                    <li><g:link action="show" params="${[ageBand:t.ageBand, teamName:t.name]}">${t.name}</g:link></li>
                    </g:if>
                    <g:else>
                    <li><strong>${t.name}</strong></li>
                    </g:else>
                    </g:each>
                </ul>
            </div>
        </div>
    
    </body>
</html>
