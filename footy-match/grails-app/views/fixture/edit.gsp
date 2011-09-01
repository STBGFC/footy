

<%@ page import="org.davisononline.footy.match.Fixture" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'fixture.label', default: 'Fixture')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${fixtureInstance}">
            <div class="errors">
                <g:renderErrors bean="${fixtureInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${fixtureInstance?.id}" />
                <g:hiddenField name="version" value="${fixtureInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="dateTime"><g:message code="fixture.dateTime.label" default="Date Time" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: fixtureInstance, field: 'dateTime', 'errors')}">
                                    <g:datePicker name="dateTime" precision="day" value="${fixtureInstance?.dateTime}" default="none" noSelection="['': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="result"><g:message code="fixture.result.label" default="Result" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: fixtureInstance, field: 'result', 'errors')}">
                                    <g:select name="result.id" from="${org.davisononline.footy.match.Result.list()}" optionKey="id" value="${fixtureInstance?.result?.id}" noSelection="['null': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="referee"><g:message code="fixture.referee.label" default="Referee" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: fixtureInstance, field: 'referee', 'errors')}">
                                    <g:select name="referee.id" from="${org.davisononline.footy.core.Person.list()}" optionKey="id" value="${fixtureInstance?.referee?.id}" noSelection="['null': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="type"><g:message code="fixture.type.label" default="Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: fixtureInstance, field: 'type', 'errors')}">
                                    <g:select name="type" from="${fixtureInstance.constraints.type.inList}" value="${fixtureInstance?.type}" valueMessagePrefix="fixture.type"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="awayTeam"><g:message code="fixture.awayTeam.label" default="Away Team" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: fixtureInstance, field: 'awayTeam', 'errors')}">
                                    <g:select name="awayTeam.id" from="${org.davisononline.footy.core.Team.list()}" optionKey="id" value="${fixtureInstance?.awayTeam?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="homeTeam"><g:message code="fixture.homeTeam.label" default="Home Team" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: fixtureInstance, field: 'homeTeam', 'errors')}">
                                    <g:select name="homeTeam.id" from="${org.davisononline.footy.core.Team.list()}" optionKey="id" value="${fixtureInstance?.homeTeam?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="postponed"><g:message code="fixture.postponed.label" default="Postponed" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: fixtureInstance, field: 'postponed', 'errors')}">
                                    <g:checkBox name="postponed" value="${fixtureInstance?.postponed}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="resources"><g:message code="fixture.resources.label" default="Resources" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: fixtureInstance, field: 'resources', 'errors')}">
                                    <g:select name="resources" from="${org.davisononline.footy.match.MatchResource.list()}" multiple="yes" optionKey="id" size="5" value="${fixtureInstance?.resources*.id}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
