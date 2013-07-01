        <g:if test="${teams.size()>0}">
        <table><tbody>
            <tr>
                <th style="width:30%">U${age} <g:if test="${teams[0].girlsTeam}">(Girls) </g:if>Competition</th>
                <th style="width:20%">League/div.</th>
                <th style="width:40%">Contact</th>
                <th style="width:10%"></th>
            </tr>
            <g:each in="${teams}" var="team">
            <tr>
               <td>${team.club} ${team.name}</td>
               <td>${team.league} / ${team.division}</td>
               <td><a href="mailto:${team.manager.email}">${team.manager}</a> (${team.manager.phone1})</td>
               <td>
                   <g:link controller='entry'
                           action='deleteTeam'
                           params='[tournamentId: tournamentInstance.id, teamId: team.id]'
                           title="Delete this entry"
                           onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">Delete</g:link>
               </td>
            </tr>
            </g:each>
        </tbody></table>
        </g:if>
