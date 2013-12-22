        <g:if test="${teams.size()>0}">
        <h3>${comp} ${waitingList ? 'Waiting List' : 'Competition'}</h3>
        <table><tbody>
            <tr>
                <th></th>
                <th style="width:20%">Team</th>
                <th style="width:10%">Entered On</th>
                <th style="width:15%">League / Team Strength</th>
                <th style="width:40%">Contact</th>
                <th style="width:10%"></th>
            </tr>
            <g:each in="${teams.sort{it.dateEntered}}" var="team">
            <tr>
               <td><footy:paymentStatus payment="${team.payment}"/></td>
               <td>${team.clubAndTeam}</td>
               <td><g:formatDate date="${team.dateEntered}" format="dd/MM/yy"/></td>
               <td>${team.league} / ${team.strength}</td>
               <td><a href="mailto:${team.contact.email}">${team.contact}</a> (${team.contact.phone1})</td>
               <td>
                   <g:link controller='entry'
                           action='delete'
                           params="${['entryId': team.id, 'tournamentId': tournamentInstance.id]}"
                           title="Delete this entry"
                           onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">Delete</g:link>
               </td>
            </tr>
            </g:each>
        </tbody></table>
        </g:if>
