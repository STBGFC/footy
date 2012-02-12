<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
        <title><g:message code="org.davisononline.org.footy.core.registration.renewal.teamselect.label" default="Renew Player Registration" /></title>
    </head>
    <body>
        <div class="dialog">
            <g:form name="registration" action="renewRegistration">
                <p>
                   <g:message
                           code="org.davisononline.footy.core.registration.renewal.teamselect.text"
                           default="Please select the team that the player whose registration you wish to renew plays for."/>
                </p>

                <table>
                    <tbody>
                        <tr class="prop">
                            <td  class="name">
                                <label for="teamId"><g:message code="org.davisononline.org.footy.core.team.label" default="Team" /></label>
                            </td>
                            <td  class="value">
                                <g:select name="teamId" from="${Team.findAllByClub(Club.homeClub, [sort: 'ageBand'])}" optionKey="id"/>
                            </td>
                        </tr>
                    </tbody>
                </table>

                <div class="buttons flowcontrol">
                    <span class="button"><g:submitButton name="continue" class="save" value="${message(code: 'default.button.continue.label', default: 'Continue')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
