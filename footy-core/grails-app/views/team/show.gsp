
<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>${teamInstance}</title>
    </head>
    <body>

        <div id="homemain">

            <div id="iconbar">
                <sec:ifAnyGranted roles="ROLE_CLUB_ADMIN">
                <g:link action="edit" id="${teamInstance.id}" title="${message(code: 'team.vedit.label', default: 'Edit this team')}">
                <img src="${createLinkTo(dir:'images', file:'vedit.png', plugin:'footy-core')}" alt="${message(code: 'team.vedit.label', default: 'Edit this team')}"/>
                </g:link>
                </sec:ifAnyGranted>

                <sec:ifAnyGranted roles="ROLE_COACH">
                <g:if test="${teamInstance?.id && players.size() > 0}">
                <g:link action="leagueForm" id="${teamInstance?.id}" title="${message(code: 'team.vprint.label', default: 'Print registration form')}">
                <img src="${createLinkTo(dir:'images', file:'vprint.png', plugin:'footy-core')}" alt="${message(code: 'team.vprint.label', default: 'Print registration form')}"/>
                </g:link>
                </g:if>
                </sec:ifAnyGranted>

                <a
                    title="${message(code: 'team.vmail.label', default: 'Email Manager/Coaches')}"
                    href="mailto:${[teamInstance.manager, teamInstance.coaches]*.email.flatten().join(",")}"
                >
                    <img src="${createLinkTo(dir:'images', file:'vmail.png', plugin:'footy-core')}" alt="${message(code: 'team.vmail.label', default: 'Email Manager/Coaches')}"/>
                </a>

                <g:link action="addresscards" id="${teamInstance.id}" title="${message(code: 'team.vmail.label', default: 'Download contact details for your address book')}">
                <img src="${createLinkTo(dir:'images', file:'vcards.png', plugin:'footy-core')}" alt="${message(code: 'team.vcards.label', default: 'Download Contact Details')}"/>
                </g:link>
            </div>

            <h2>Latest News</h2>

            <h2>Players</h2>
            <sec:ifAnyGranted roles="ROLE_COACH">
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
            </sec:ifAnyGranted>

            <sec:ifNotGranted roles="ROLE_COACH">
            <p>
                ${players.join(", ")}
            </p>
            </sec:ifNotGranted>

            <div id="otherteams">
                <ul>
                    <g:each in="${Team.findAllByAgeBand(teamInstance.ageBand)}" var="t">
                    <g:if test="${t != teamInstance}">
                    <li><g:link action="show" params="${[ageBand:t.ageBand, teamName:t.name]}">${t}</g:link></li>
                    </g:if>
                    <g:else>
                    <li><strong>${t}</strong></li>
                    </g:else>
                    </g:each>
                </ul>
            </div>
        </div>


        <div id="newspanel">
            <div class="newsbox">
                <h2>${teamInstance}</h2>
                <ul>
                    <li>League: <strong>${teamInstance.league}</strong></li>
                    <g:if test="${teamInstance.division}">
                    <li>Division: <strong>${teamInstance.division}</strong></li></g:if>
                </ul>
                <ul>
                    <li>Manager: <a href="mailto:${teamInstance.manager.email}" title="Send Email to ${teamInstance.manager}">${teamInstance.manager}</a></li>
                    <g:each in="${teamInstance.coaches}" var="c">
                    <li>Coach: <a href="mailto:${c.email}" title="Send Email to ${c}">${c}</a></li>
                    </g:each>
                </ul>
            </div>
            <div class="newsbox">
                <h2>Event Calendar</h2>
            </div>
        </div>
    
    </body>
</html>
