
<%@ page import="org.davisononline.footy.match.Fixture" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'fixture.label', default: 'Fixture')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'fixture.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="dateTime" title="${message(code: 'fixture.dateTime.label', default: 'Date Time')}" />
                        
                            <th><g:message code="fixture.result.label" default="Result" /></th>
                        
                            <th><g:message code="fixture.referee.label" default="Referee" /></th>
                        
                            <g:sortableColumn property="type" title="${message(code: 'fixture.type.label', default: 'Type')}" />
                        
                            <th><g:message code="fixture.awayTeam.label" default="Away Team" /></th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${fixtureInstanceList}" status="i" var="fixtureInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${fixtureInstance.id}">${fieldValue(bean: fixtureInstance, field: "id")}</g:link></td>
                        
                            <td><g:formatDate date="${fixtureInstance.dateTime}" /></td>
                        
                            <td>${fieldValue(bean: fixtureInstance, field: "result")}</td>
                        
                            <td>${fieldValue(bean: fixtureInstance, field: "referee")}</td>
                        
                            <td>${fieldValue(bean: fixtureInstance, field: "type")}</td>
                        
                            <td>${fieldValue(bean: fixtureInstance, field: "awayTeam")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${fixtureInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
