
                        <tr class="prop">
                            <td  class="name">
                                <label for="leagueRegistrationNumber"><g:message code="org.davisononline.footy.core.leagueRegistration.label" default="League Registration Number" /></label>
                            </td>
                            <td  class="value">
                                <g:textField name="leagueRegistrationNumber" value="${playerInstance?.leagueRegistrationNumber}"/>
                            </td>
                        </tr>

                        <tr class="prop">
                            <td  class="name">
                                <label for="team.id"><g:message code="org.davisononline.footy.core.team.label" default="Team" /></label>
                            </td>
                            <td  class="value">
                                <g:select name="team.id" from="${availableTeams}" noSelection="['null':'-- unassigned --']" optionKey="id" value="${playerInstance?.team?.id}"/>
                            </td>
                        </tr>
