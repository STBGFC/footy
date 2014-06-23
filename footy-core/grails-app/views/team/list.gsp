
<%@ page import="org.davisononline.footy.core.Team" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'team.label', default: 'Team')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
        <export:resource/>
    </head>
    <body>
        <h1>
            <g:message code="org.davisononoline.footy.core.teamlistview.text" default="Teams at the club"/>
        </h1>
        <div class="list">
            <div class="nav">
                <span class="menuButton"><a class="home" href="${createLink(uri: '/login/profile')}"><g:message code="default.home.label"/></a></span>
                <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </div>
            <table class="list">
                <thead>
                    <tr>
                        <g:sortableColumn property="ageBand" title="${message(code: 'team.name.label', default: 'Name')}" />
                        <g:sortableColumn property="division" title="${message(code: 'team.division.label', default: 'Division')}" />
                        <g:sortableColumn property="manager" title="${message(code: 'team.manager.label', default: 'Manager')}" />
                        <th><g:message code="team.coaches.label" default="Coaches"/></th>
                    </tr>
                </thead>
                <tbody>
                <g:each in="${teamInstanceList}" status="i" var="teamInstance">
                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        <td><g:link action="edit" id="${teamInstance.id}">${teamInstance}</g:link></td>
                        <td>
                            <g:if test="${teamInstance.division}">
                            <g:link controller="division" action="edit" id="${teamInstance.division.id}">${teamInstance?.division}</g:link>
                            </g:if>
                        </td>
                        <td><g:link controller="person" action="edit" id="${teamInstance.manager.id}">${teamInstance.manager}</g:link> (${teamInstance.manager.phone1})</td>
                        <td>
                            <g:each in="${teamInstance.coaches}" var="coach">
                            <g:link controller="person" action="edit" id="${coach.id}">${coach}</g:link> (${coach.phone1})<br/>
                            </g:each>
                        </td>
                    </tr>
                </g:each>
                </tbody>
            </table>
            <div class="paginateButtons">
                <g:paginate total="${teamInstanceTotal}" />
            </div>
            <export:formats formats="['excel', 'pdf']" />
        </div>
    </body>
</html>
