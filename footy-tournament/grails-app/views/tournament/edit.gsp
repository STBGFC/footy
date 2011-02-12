<%@ page import="org.davisononline.footy.tournament.Tournament" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'tournament.label', default: 'Tournament')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
            <g:hasErrors bean="${tournamentInstance}">
            <div class="errors">
                <g:renderErrors bean="${tournamentInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:if test="${tournamentInstance?.id}">
                <g:hiddenField name="id" value="${tournamentInstance?.id}" />
                <g:hiddenField name="version" value="${tournamentInstance?.version}" />
                </g:if>
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="tournament.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tournamentInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${tournamentInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="startDate"><g:message code="tournament.startDate.label" default="Start Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tournamentInstance, field: 'startDate', 'errors')}">
                                    <g:datePicker name="startDate" precision="day" value="${tournamentInstance?.startDate}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="endDate"><g:message code="tournament.endDate.label" default="End Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tournamentInstance, field: 'endDate', 'errors')}">
                                    <g:datePicker name="endDate" precision="day" value="${tournamentInstance?.endDate}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="costPerTeam"><g:message code="tournament.costPerTeam.label" default="Cost Per Team" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tournamentInstance, field: 'costPerTeam', 'errors')}">
                                    <g:textField name="costPerTeam" value="${fieldValue(bean: tournamentInstance, field: 'costPerTeam')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="openForEntry"><g:message code="tournament.openForEntry.label" default="Open For Entry" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tournamentInstance, field: 'openForEntry', 'errors')}">
                                    <g:checkBox name="openForEntry" value="${tournamentInstance?.openForEntry}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <g:if test="${tournamentInstance?.id}">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                    </g:if>
                    <g:else>
                    <span class="button"><g:actionSubmit class="save" action="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                    </g:else>
                </div>
            </g:form>
    </body>
</html>
