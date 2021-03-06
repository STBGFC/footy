
<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'team.label', default: 'Team')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <h1>
            Create or edit details of the team below
        </h1>
        <div class="dialog">
            <div class="nav">
                <g:render template="/shared/editNavButtons" model="${[entityName:entityName]}"/>
            </div>

            <g:form name="team" method="post" >
                <g:hiddenField name="id" value="${teamInstance?.id}" />
                <g:hiddenField name="version" value="${teamInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                            <g:render template="teamFormBody" model="[teamCommand: teamInstance, clubInstance: Club.getHomeClub()]" plugin="footy-core"/>

                            <tr class="prop">
                                <td  class="name">
                                    <label for="sponsor.id"><g:message code="entry.sponsor.label" default="Sponsor" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: teamInstance, field: 'sponsor', 'errors')}">
                                    <g:select name="sponsor.id" from="${Sponsor.list()}" value="${teamInstance?.sponsor?.id}" optionKey="id" noSelection="${['null':'-- none --']}"/>
                                    <g:render template="/shared/fieldError" model="['instance':teamInstance,'field':'sponsor']" plugin="footy-core"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="manager.id"><g:message code="org.davisononline.footy.core.team.manager.label" default="Manager" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: teamInstance, field: 'manager', 'errors')}">
                                    <g:select name="manager.id" from="${managers}" optionKey="id" value="${teamInstance?.manager?.id}"  />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="coaches"><g:message code="org.davisononline.footy.core.team.coaches.label" default="Coaches" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: teamInstance, field: 'coaches', 'errors')}">
                                    <g:select name="coaches" from="${managers}" optionKey="id" multiple="yes" size="8" value="${teamInstance?.coaches*.id}"  />
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <g:if test="${teamInstance?.id}">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button">
                        <g:actionSubmit
                                class="delete"
                                action="delete"
                                value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                onclick="return confirm('${message(code: 'team.button.delete.confirm.message', default: 'This will remove all fixtures and referee reports and set any current players to have no team.  Are you sure?')}');" />
                    </span>
                    </g:if>
                    <g:else>
                    <span class="button"><g:actionSubmit class="save" action="save" value="${message(code: 'default.button.save.label', default: 'Save')}" /></span>
                    </g:else>
                </div>
            </g:form>
        </div>
    </body>
</html>
