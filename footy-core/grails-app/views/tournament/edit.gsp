<%@ page import="org.davisononline.footy.core.Person; org.davisononline.footy.tournament.Tournament" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'tournament.label', default: 'Tournament')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="dialog">
        <div class="nav">
            <g:render template="/shared/editNavButtons" model="${[entityName:entityName]}"/>
            <span class="menuButton">
                <g:link class="create" controller="competition" action="create" params="${['tournament.id':tournamentInstance.id]}"><g:message code="default.new.label" args="['Competition']" /></g:link>
            </span>
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
                <g:if test="${tournamentInstance.hasEntries()}">
                <div class="notice">
                    <g:message code="org.davisononline.footy.tournament.views.tournament.edit.entriesexist" default="NB: Entries already exist for this tournament!"/>
                </div>
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
                                <td valign="top" class="value date ${hasErrors(bean: tournamentInstance, field: 'startDate', 'errors')}">
                                    <g:datePicker name="startDate" precision="day" value="${tournamentInstance?.startDate}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="endDate"><g:message code="tournament.endDate.label" default="End Date" /></label>
                                </td>
                                <td valign="top" class="value date ${hasErrors(bean: tournamentInstance, field: 'endDate', 'errors')}">
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

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="treasurer.id"><g:message code="tournament.treasurer.label" default="Treasurer (person to send cheques to)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tournamentInstance, field: 'treasurer', 'errors')}">
                                    <g:select name="treasurer.id" from="${treasurerCandidates}" noSelection="[null:'-- None Needed --']" value="${tournamentInstance.treasurer?.id}" optionKey="id" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="cclist"><g:message code="tournament.cclist.label" default="cc List on confirmation emails" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tournamentInstance, field: 'cclist', 'errors')}">
                                    <g:textField name="cclist" value="${tournamentInstance?.cclist}" />
                                </td>
                            </tr>

                            <g:if test="${tournamentInstance.id}">
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label><g:message code="tournament.applyLink.label" default="Link to Enter" /></label>
                                </td>
                                <td valign="top" class="value">
                                    <g:createLink absolute="true" action="signup" controller="tournament" params='[name: "${tournamentInstance.name.encodeAsHTML()}"]'/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label><g:message code="tournament.competitions.label" default="Competitions/Sections" /></label>
                                </td>
                                <td valign="top" class="value">
                                    <ul>
                                    <g:each in="${tournamentInstance.competitions.sort{it.name}}" var="c">
                                        <li><g:link controller="competition" action="edit" id="${c.id}">${c}</g:link></li>
                                    </g:each>
                                    </ul>
                                </td>
                            </tr>
                            </g:if>
                        
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
        </div>
    </body>
</html>
