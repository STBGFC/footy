
<%@ page import="org.davisononline.footy.core.Person" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'player.label', default: 'Player')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="list">
            <p>
                Players who play with the club
            </p>
            <div class="nav">
                <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
                <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </div>
            
                <table class="list">
                    <thead>
                        <tr>
                            <g:sortableColumn property="person.familyName" title="${message(code: 'person.name.label', default: 'Name')}" />
                            <g:sortableColumn property="dateOfBirth" title="${message(code: 'player.dateOfBirth.label', default: 'DoB')}" />
                            <g:sortableColumn property="dateJoinedClub" title="${message(code: 'player.dateJoinedClub.label', default: 'Joined Club')}" />
                            <g:sortableColumn property="lastRegistrationDate" title="${message(code: 'player.lastRegistrationDate.label', default: 'Last Registration')}" />
                            <g:sortableColumn property="leagueRegistrationNumber" title="${message(code: 'player.leagueRegistrationNumber.label', default: 'League Registration #')}" />
                            <th>Team</th>
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${playerInstanceList}" status="i" var="player">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                            <td><g:link action="editplayer" id="${player.id}">${player.person}</g:link></td>
                            <td><g:formatDate date="${player.dateOfBirth}" format="dd/MM/yyyy"/></td>
                            <td><g:formatDate date="${player.dateJoinedClub}" format="dd/MM/yyyy"/></td>
                            <td><g:formatDate date="${player.lastRegistrationDate}" format="dd/MM/yyyy"/></td>
                            <td>${fieldValue(bean: player, field: "leagueRegistrationNumber")}</td>
                            <td>${fieldValue(bean: player, field: "team")}</td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${playerInstanceTotal}" />
            </div>
    </body>
</html>
