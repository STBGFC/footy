
<%@ page import="org.davisononline.footy.core.Division; org.davisononline.footy.core.League" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'league.label', default: 'League')}" />
        <title>
            <g:if test="${leagueInstance?.id}">${leagueInstance.name}</g:if>
            <g:else><g:message code="default.create.label" args="[entityName]" /></g:else>
        </title>
    </head>
    <body>
        <div class="dialog">
            <p>
                Create or edit the league and division details below
            </p>
            <div class="nav">
                <g:render template="/shared/editNavButtons" model="${[entityName:entityName]}"/>
                <span class="menuButton">
                    <g:link class="create" controller="division" action="create" params="${['league.id':leagueInstance.id]}"><g:message code="default.new.label" args="['Division']" /></g:link>
                </span>
            </div>

            <g:form method="post"  enctype="multipart/form-data">
                <g:hiddenField name="id" value="${leagueInstance?.id}" />
                <g:hiddenField name="version" value="${leagueInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="league.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: leagueInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" maxlength="50" value="${leagueInstance?.name}" />
                                    <g:render template="/shared/fieldError" model="['instance':leagueInstance,'field':'name']" plugin="footy-core"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label><g:message code="league.divisions.label" default="Divisions" /></label>
                                </td>
                                <td valign="top" class="value">
                                    <ul>
                                    <g:each in="${Division.findAllByLeague(leagueInstance)}" var="d">
                                        <li><g:link controller="division" action="edit" id="${d.id}">U${d.ageBand} ${d.name}</g:link></li>
                                    </g:each>
                                    </ul>
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <g:if test="${leagueInstance?.id}">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                    </g:if>
                    <g:else>
                    <span class="button"><g:actionSubmit class="save" action="save" value="${message(code: 'default.button.save.label', default: 'Save')}" /></span>
                    </g:else>
                </div>
            </g:form>
        </div>
    </body>
</html>
