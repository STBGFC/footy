
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
            <g:each in="${tournamentInstanceList}" status="i" var="tournamentInstance">
                <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                    <td class="${tournamentInstance.openForEntry ? "openTournament" : "closedTournament"}">${tournamentInstance.name}</td>
                    <td><g:formatDate date="${tournamentInstance.startDate}" format="dd/MM/yyyy"/></td>
                    <td><g:formatNumber currencyCode="GBP" type="currency" number="${tournamentInstance.costPerTeam}"/></td>
                    <td>
                        <g:if test="${!tournamentInstance.entries}">
                        <g:link action="edit" id="${tournamentInstance.id}"><img alt="edit details" title="edit details" src="${resource(dir:'images',file:'edit.png')}"/></g:link>
                        </g:if>
                        <g:else>
                        <g:link action="show" id="${tournamentInstance.id}"><img alt="view teams" title="view teams" src="${resource(dir:'images',file:'view.png')}"/></g:link>
                        <g:link action="entryList" id="${tournamentInstance.id}"><img alt="payment status" title="payment status" src="${resource(dir:'images',file:'payments.png')}"/></g:link>
                        </g:else>
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
