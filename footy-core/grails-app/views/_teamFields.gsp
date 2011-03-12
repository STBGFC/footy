<%@ page import="org.davisononline.footy.core.*" %>
                        <tr class="prop">
                            <td  class="name">
                                <label for="leagueRegistrationNumber"><g:message code="org.davisononline.footy.core.leagueRegistration.label" default="League Registration Number" /></label>
                            </td>
                            <td  class="value">
                                <%-- TODO: should have a unique constraint --%>
                                <g:textField name="leagueRegistrationNumber" value="${playerInstance?.leagueRegistrationNumber}"/>
                            </td>
                        </tr>

                        <tr class="prop">
                            <td  class="name">
                                <label for="team.id"><g:message code="org.davisononline.footy.core.team.label" default="Team" /></label>
                            </td>
                            <td  class="value">
                                <%-- TODO: ensure teams are from correct age band and home club only --%>
                                <g:select name="team.id" from="${Team.findAllByClub(Club.getHomeClub())}" noSelection="['null':'-- unassigned --']" optionKey="id" value="${playerInstance?.team?.id}"/>
                            </td>
                        </tr>
