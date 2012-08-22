<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
        <title><g:message code="org.davisononline.org.footy.core.registration.renewal.selectplayer.label" default="Select Player" /></title>
    </head>
    <body>
        <h3>
           <g:message
                   code="org.davisononline.footy.core.registration.renew.selectplayer.text"
                   default="Please select the player to renew a registration for"/>
        </h3>
        <div class="dialog">
            <g:form name="registration" action="renewRegistration">

                <table>
                    <tbody>
                        <tr class="prop">
                            <td  class="name">
                                <label for="playerId"><g:message code="org.davisononline.org.footy.core.player.label" default="Player" /></label>
                            </td>
                            <td  class="value">
                                <g:select name="playerId" from="${players}" optionKey="id"/>
                            </td>
                        </tr>
                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="dateOfBirth"><g:message code="org.davisononline.footy.registration.playerDob.label" default="Date of Birth" /></label>
                            </td>
                            <td valign="top" class="value date">
                                <g:set var="now" value="${new Date()}"/>
                                <g:datePicker name="dateOfBirth" precision="day" noSelection="[null:'-select-']" years="${(now.year-19+1900)..(now.year-2+1900)}" value="${null}"  />
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
