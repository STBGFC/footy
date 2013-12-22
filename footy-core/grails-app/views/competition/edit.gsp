
<%@ page import="org.davisononline.footy.tournament.Competition" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'competition.label', default: 'Competition')}" />
        <title>
            <g:if test="${competitionInstance?.id}">${competitionInstance.name}</g:if>
            <g:else><g:message code="default.create.label" args="[entityName]" /></g:else>
        </title>
    </head>
    <body>
        <h1>
            Create or edit the competition details below
        </h1>
        <div class="dialog">
            <div class="nav">
                <g:render template="/shared/editNavButtons" model="${[entityName:entityName]}"/>
            </div>

            <g:form method="post"  enctype="multipart/form-data">
                <g:hiddenField name="id" value="${competitionInstance?.id}" />
                <g:hiddenField name="version" value="${competitionInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <g:message code="competition.tournament.label" default="tournament"/>
                                </td>
                                <td valign="top" class="value">
                                    ${competitionInstance.tournament}
                                    <g:hiddenField name="tournament.id" value="${competitionInstance?.tournament.id}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="competition.name.label" default="Name"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: competitionInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" maxlength="30" value="${competitionInstance?.name}"/>
                                    <g:render template="/shared/fieldError" model="['instance':competitionInstance,'field':'name']" plugin="footy-core"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="gameFormat"><g:message code="competition.format.label" default="Game Format"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: competitionInstance, field: 'gameFormat', 'errors')}">
                                    <g:select name="gameFormat" value="${competitionInstance?.gameFormat}" from="${Competition.GAME_FORMATS}" />
                                    <g:render template="/shared/fieldError" model="['instance':competitionInstance,'field':'gameFormat']" plugin="footy-core"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="teamLimit"><g:message code="competition.code.label" default="Max. Teams"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: competitionInstance, field: 'teamLimit', 'errors')}">
                                    <g:textField name="teamLimit" value="${competitionInstance?.teamLimit}"/>
                                    <g:render template="/shared/fieldError" model="['instance':competitionInstance,'field':'teamLimit']" plugin="footy-core"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="open"><g:message code="competition.open.label" default="Open For Entry" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: competitionInstance, field: 'open', 'errors')}">
                                    <g:checkBox name="open" value="${competitionInstance?.open}" />
                                </td>
                            </tr>


                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <g:if test="${competitionInstance?.id}">
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