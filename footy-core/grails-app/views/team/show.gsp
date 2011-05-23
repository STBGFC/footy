
<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>${teamInstance}</title>
    </head>
    <body>

        <div id="homemain">
            <h2>Team Info</h2>

            <g:link action="addresscards" id="${teamInstance.id}" title="${message(code: 'team.vcards.label', default: 'Download Contact Details')}">
                <img id="vcards" src="${createLinkTo(dir:'images', file:'vcards.png', plugin:'footy-core')}" alt="${message(code: 'team.vcards.label', default: 'Download Contact Details')}"/>
            </g:link>
            <p>
                <strong>Manager:</strong> <a href="mailto:${teamInstance.manager.email}" title="Send Email to ${teamInstance.manager}">${teamInstance.manager}</a>
            </p>
            <p>
                <strong>Coaches:</strong>
                <g:each in="${teamInstance.coaches}" var="c">
                <a href="mailto:${c.email}" title="Send Email to ${c}">${c}</a>
                </g:each>
            </p>
            <sec:ifAnyGranted roles="ROLE_COACH">
            <p>
                <strong>Players:</strong>
                <table class="list">
                    <thead>
                        <tr>
                            <th>${message(code: 'person.name.label', default: 'Name')} (${message(code: 'player.dateOfBirth.label', default: 'DoB')})</th>
                            <th>${message(code: 'player.medical.label', default: 'Medical')}</th>
                            <th>${message(code: 'player.contactDetails.label', default: 'Contact')}</th>
                            <th>${message(code: 'player.leagueRegistrationNumber.label', default: 'Registration')}</th>
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${players}" status="i" var="player">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                            <td>
                                <g:link controller="player" action="edit" id="${player.id}">${player.person}</g:link>
                                <br/>(<g:formatDate date="${player.dateOfBirth}" format="dd/MM/yyyy"/>)
                            </td>
                            <td>${player.medical}</td>
                            <td>
                                <a href="mailto:${player.guardian?.email}" title="Send Email to ${player.guardian}">${player.guardian}</a>
                                <br/>${player.guardian?.phone1}
                            </td>
                            <td>${fieldValue(bean: player, field: "leagueRegistrationNumber")}</td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </p>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_CLUB_ADMIN">
            <div class="nav">
                <span class="menuButton"><g:link class="edit" action="edit" id="${teamInstance.id}">edit this team</g:link></span>
            </div>
            </sec:ifAnyGranted>

            <sec:ifNotGranted roles="ROLE_COACH">
            <p>
                <strong>Players:</strong> ${players.join(", ")}
            </p>
            </sec:ifNotGranted>
            
        </div>


        <div id="newspanel">
            <div class="newsbox">
                <h2>${teamInstance} Upcoming Events</h2>
                <ul>
                    <li>none found</li>
                </ul>
            </div>
            <div class="newsbox">
                <h2>All U${teamInstance.ageBand} Teams</h2>
                <ul>
                    <g:each in="${Team.findAllByAgeBand(teamInstance.ageBand)}" var="t">
                    <g:if test="${t != teamInstance}">
                    <li><g:link action="show" params="${[ageBand:t.ageBand, teamName:t.name]}">${t.name}</g:link></li>
                    </g:if>
                    <g:else>
                    <li><strong>${t.name}</strong></li>
                    </g:else>
                    </g:each>
                </ul>
            </div>
        </div>
    
    </body>
</html>
