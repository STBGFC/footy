<%@ page import="org.davisononline.footy.core.*" %>
                        <tr class="prop">
                            <td  class="name">
                                <label for="leagueRegistrationNumber"><g:message code="org.davisononline.footy.core.leagueRegistration.label" default="League Registration Number" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: playerInstance, field: 'leagueRegistrationNumber', 'errors')}">
                                <g:textField name="leagueRegistrationNumber" value="${playerInstance?.leagueRegistrationNumber}"/>
                                <g:render template="/fieldError" model="['instance':playerInstance,'field':'leagueRegistrationNumber']"/>
                            </td>
                        </tr>

                        <tr class="prop">
                            <td  class="name">
                                <label for="team.id"><g:message code="org.davisononline.footy.core.team.label" default="Team" /></label>
                            </td>
                            <td  class="value">
                                <%-- TODO: ensure teams are from correct age band and home club only --%>
                                <g:select name="team.id" from="${validTeams}" noSelection="['null':'-- unassigned --']" optionKey="id" value="${playerInstance?.team?.id}"/>
                            </td>
                        </tr>
