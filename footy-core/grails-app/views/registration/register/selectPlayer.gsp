<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
        <title><g:message code="org.davisononline.org.footy.core.registration.selectplayer.title" default="Select Player" /></title>
    </head>
    <body>
        <h1>
           <g:message
                   code="org.davisononline.footy.core.registration.selectplayer.title"
                   default="Select player"/>
        </h1>
        <div class="dialog">
            <g:form name="registration" action="register">

                <g:message code="org.davisononline.footy.core.registration.selectplayer.body" />
                <g:submitButton name="addPlayer" class="edit" value="${message(code: 'org.davisononline.footy.core.registration.addplayer.button.label', default: 'Add new player')}" />
                <g:if test="${duplicate}"><span class="error"><g:message code="org.davisononline.footy.core.registration.duplicate" default="A player with that name and date of birth already exists"/></span></g:if>
                <table>
                    <tbody>
                        <g:each var="player" in="${playersAvailable}">
                        <tr class="prop">
                            <td  class="name">
                                <strong>${player}: ${player.team ?: ""}</strong> <br/>
                                (current/last expiry date: ${player.currentRegistration ? g.formatDate(date:player.currentRegistration.date, format:"yyyy-MM-dd") : 'n/a'})
                                <g:hiddenField name="playerId" value="${player.id}"/>
                            </td>
                            <td class="value">
                                <g:if test="${player.currentRegistration?.date > new Date()}">
                                    registration is currently up to date
                                    <g:hiddenField name="regTierId" value="x"/>
                                </g:if>
                                <g:else>
                                    <g:select name="regTierId" from="${tiers}" optionKey="id" noSelection="${['x':'-- Do Not Register --']}"/>
                                </g:else>
                            </td>
                        </tr>
                        </g:each>
                    </tbody>
                </table>

                <div class="buttons flowcontrol">
                    <span class="button"><g:submitButton name="continue" class="save" value="${message(code: 'default.button.continue.label', default: 'Continue')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
