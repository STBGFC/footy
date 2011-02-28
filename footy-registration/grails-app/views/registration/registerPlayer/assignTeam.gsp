<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
        <title><g:message code="default.paymentdetails.label" default="Assign Team" /></title>
    </head>
    <body>
        <div class="dialog">
            <g:form action="registerPlayer"> 
                <p>
                    If <strong>${playerCommand.givenName}</strong> is already assigned to a team and has a
                    league registration number, you can enter those here. If
                    you don't know them, just hit "${message(code: 'default.button.continue.label', default: 'Continue')}"
                </p>

                <table>
                    <tbody>

                        <tr class="prop">
                            <td  class="name">
                                <label for="leagueRegistration"><g:message code="org.davisononline.footy.core.leagueRegistration.label" default="League Registration Number" /></label>
                            </td>
                            <td  class="value">
                                <g:textField name="leagueRegistration" />
                            </td>
                        </tr>

                        <tr class="prop">
                            <td  class="name">
                                <label for="teamId"><g:message code="org.davisononline.footy.core.team.label" default="Team" /></label>
                            </td>
                            <td  class="value">
                                <%-- TODO: teams should be from correct age band (+1) --%>
                                <g:select name="teamId" from="${Team.findByClub(Club.getHomeClub())}" noSelection="['-1':'-- unassigned --']" optionKey="id" />
                            </td>
                        </tr>

                    </tbody>
                </table>
        
                <div class="buttons">
                    <span class="button"><g:submitButton name="continue" class="save" value="${message(code: 'default.button.continue.label', default: 'Continue')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
        