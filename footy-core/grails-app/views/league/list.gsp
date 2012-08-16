
<%@ page import="org.davisononline.footy.core.Division; org.davisononline.footy.core.League" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'league.label', default: 'League')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="list">
            <p>
                <g:message code="org.davisononoline.footy.core.leaguelistview.text" default="League list"/>
            </p>
            <div class="nav">
                <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
                <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </div>

            <div class="list">
                <table>
                    <thead>
                        <tr>
                            <g:sortableColumn property="name" title="${message(code: 'league.name.label', default: 'Name')}" />
                            <th>&nbsp;</th>
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${leagueInstanceList}" status="i" var="leagueInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                            <td><g:link action="edit" id="${leagueInstance.id}">${fieldValue(bean: leagueInstance, field: "name")}</g:link></td>
                            <td>${leagueInstance.divisions.size()} divisions created</td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${leagueInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
