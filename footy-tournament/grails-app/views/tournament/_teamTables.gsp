        <g:if test="${teams.size()>0}">
        <table><tbody>
            <tr>
                <th style="width:30%">U${age} <g:if test="${teams[0].girlsTeam}">(Girls) </g:if>Competition</th>
                <th style="width:20%">League/div.</th>
                <th style="width:50%">Contact</th>
            </tr>
            <g:each in="${teams}" var="team">
            <tr>
               <td>${team.club} ${team.name}</td>
               <td>${team.league} / ${team.division}</td>
               <td><a href="mailto:${team.manager.email}">${team.manager}</a> (${team.manager.phone1})</td>
            </tr>
            </g:each>
        </tbody></table>
        </g:if>
