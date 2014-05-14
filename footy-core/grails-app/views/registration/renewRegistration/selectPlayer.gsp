<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
        <title><g:message code="org.davisononline.org.footy.core.registration.renewal.selectplayer.label" default="Select Player" /></title>
    </head>
    <body>
        <h1>
           <g:message
                   code="org.davisononline.footy.core.registration.renew.selectplayer.text"
                   default="Select player(s)"/>
        </h1>
        <div class="dialog">
            <g:form name="registration" action="renewRegistration">

                <p>
                    For each player you wish to re-register, select the
                    correct registration tier based on age range for the FORTHCOMING season.  It is <strong>very
                    important</strong> to select the correct tier.
                </p>

                <table>
                    <tbody>
                        <g:each var="player" in="${playersAvailable}">
                        <tr class="prop">
                            <td  class="name">
                                <strong>${player} - ${player.team}</strong> <br/>
                                (current/last expiry date: <g:formatDate date="${player.currentRegistration.date}" format="yyyy-MM-dd"/>)
                            </td>
                            <td class="value">
                                <g:if test="${player.currentRegistration?.date > new Date()}">
                                    registration is currently up to date
                                </g:if>
                                <g:else>
                                    <g:hiddenField name="playerId" value="${player.id}"/>
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
