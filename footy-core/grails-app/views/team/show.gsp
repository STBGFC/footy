
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
                <h2>Upcoming Events</h2>
                <ul>
                    <li>21/3/11 6pm-7pm: <strong>Training</strong></li>
                    <li>12/5/11 2pm-5pm: <strong>Squad barbecue</strong></li>
                    <li>13/5/11 9am: <strong>Match (Hawley Raiders AWAY)</strong></li>
                </ul>
            </div>
        </div>
    
    </body>
</html>
