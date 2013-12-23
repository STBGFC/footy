<%@ page import="org.davisononline.footy.tournament.Competition" %>
        <g:if test="${teams.size()>0}">
        <p/>
        <g:set var="placesLeft" value="${comp.teamLimit - comp.entered.size()}"/>
        <g:set var="plural" value="${placesLeft != 1 ? 's' : ''}"/>
        <h3>${comp} ${waitingList ? 'Waiting List' : "Competition: $placesLeft place$plural remaining"}</h3>
        <table><tbody>
            <tr>
                <th></th>
                <th style="width:20%">Team</th>
                <th style="width:10%">Entered On</th>
                <th style="width:15%">League / Team Strength</th>
                <th style="width:30%">Contact</th>
                <th style="width:20%"></th>
            </tr>
            <g:each in="${teams.sort{it.dateEntered}}" var="team">
            <tr>
               <td><footy:paymentStatus payment="${team.payment}"/></td>
               <td>${team.clubAndTeam}</td>
               <td><g:formatDate date="${team.dateEntered}" format="dd/MM/yy"/></td>
               <td>${team.league} / ${team.strength}</td>
               <td><a href="mailto:${team.contact.email}">${team.contact}</a> (${team.contact.phone1})</td>
               <td>
                   <g:if test="${!waitingList && comp.teamLimit <= comp.entered.size()}">
                   <g:link controller='tournament'
                           action='relegateEntry'
                           params="${['entryId': team.id, 'compId': comp.id, 'tournamentId': tournamentInstance.id]}"
                           title="Relegate this entry from the main list to the waiting list">Move to waiting list</g:link> |
                   </g:if>
                   <g:elseif test="${waitingList}">
                   <g:link controller='tournament'
                           action='promoteEntry'
                           params="${['entryId': team.id, 'compId': comp.id, 'tournamentId': tournamentInstance.id]}"
                           title="Promote this entry from the waiting list to the main list">Move to entered list</g:link> |
                   </g:elseif>
                   <g:link controller='tournament'
                           action='deleteEntry'
                           params="${['entryId': team.id, 'compId': comp.id, 'tournamentId': tournamentInstance.id]}"
                           title="Delete this entry"
                           onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">Delete</g:link>
               </td>
            </tr>
            </g:each>
        </tbody></table>
        </g:if>
