
<%@ page import="org.davisononline.footy.tournament.Tournament" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'tournament.label', default: 'Tournament')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="list">
        <p>
            A list of all tournaments is shown below.  Tournament names with a green background are open
            for entry and people can register new teams for them.  Remember to close off tournament entries!
        </p>
        <p>
            Once teams have registered for a tournament, you will no longer be able to edit the details of
            that tournament - including entry fee and dates
        </p>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <table class="list">
            <thead>
                <tr>
                    <g:sortableColumn property="name" title="${message(code: 'tournament.name.label', default: 'Name')}" />
                    <g:sortableColumn property="startDate" title="${message(code: 'tournament.startDate.label', default: 'Start Date')}" />
                    <g:sortableColumn property="costPerTeam" title="${message(code: 'tournament.costPerTeam.label', default: 'Cost Per Team')}" />
                    <th>actions</th>
                </tr>
            </thead>
            <tbody>
            <g:each in="${tournamentInstanceList}" status="i" var="tourney">
                <tr class="${(i % 2) == 0 ? 'odd' : 'even'} ${tourney.openForEntry ? "openTournament" : ""}">
                    <td><g:link action="edit" id="${tourney.id}">${tourney.name}</g:link></td>
                    <td><g:formatDate date="${tourney.startDate}" format="dd/MM/yyyy"/></td>
                    <td><g:formatNumber currencyCode="GBP" type="currency" number="${tourney.costPerTeam}"/></td>
                    <td>
                        <g:if test="${tourney.entries}">
                        <g:link action="entryList" id="${tourney.id}">view payments</g:link> |
                        <g:link action="show" id="${tourney.id}">show entries</g:link>
                        </g:if>
                    </td>
                </tr>
            </g:each>
            </tbody>
        </table>
        <div class="paginateButtons">
            <g:paginate total="${tournamentInstanceTotal}" />
        </div>
    </div>
    </body>
</html>
