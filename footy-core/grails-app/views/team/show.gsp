
<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>${teamInstance}</title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'bubbletips.css',plugin:'footy-core')}" />
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

            <h2>Latest News &amp; Results</h2>
            <p>
                No news yet.
            </p>
            <h2>${teamInstance.players.size()} Players</h2>
            <footy:isManager team="${teamInstance}">
            <table class="list">
                <thead>
                    <tr>
                        <th>${message(code: 'person.name.label', default: 'Name')} (${message(code: 'player.dateOfBirth.label', default: 'DoB')})</th>
                        <th>${message(code: 'player.medical.label', default: 'Medical')}</th>
                        <th>${message(code: 'player.contactDetails.label', default: 'Contact')}</th>
                        <th>${message(code: 'player.leagueRegistrationNumber.label', default: 'Registration #')}</th>
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
                            <g:link controller="person" action="edit" id="${player.guardian.id}">${player.guardian}</g:link>
                            <br/>${player.guardian?.bestPhone().encodeAsHTML()}
                            <br/><a class="email" href="mailto:${player.guardian?.email}" title="Send Email to ${player.guardian}">${player.guardian.email}</a>
                        </td>
                        <td>${fieldValue(bean: player, field: "leagueRegistrationNumber")}</td>
                    </tr>
                </g:each>
                </tbody>
            </table>
            </footy:isManager>

            <footy:isNotManager team="${teamInstance}">
            <p>
                ${players.join(", ")}
            </p>
            </footy:isNotManager>
        </div>


        <div id="newspanel">

            <footy:isManager team="${teamInstance}">
            <modalbox:createLink
                    controller="team"
                    action="photoUploadDialog"
                    id="${teamInstance.id}"
                    title="Team Photo Upload"
                    width="400">
                <footy:teamPhoto team="${teamInstance}"/>
            </modalbox:createLink>
            </footy:isManager>
            <footy:isNotManager team="${teamInstance}">
            <footy:teamPhoto team="${teamInstance}"/>
            </footy:isNotManager>

            <div class="newsbox">
                <h2>${teamInstance}</h2>
                <ul>
                    <li>League: <strong>${teamInstance.league}</strong></li>
                    <g:if test="${teamInstance.division}">
                    <li>Division: <strong>${teamInstance.division}</strong></li></g:if>
                </ul>
                <ul>
                    <li>Manager:
                        <footy:tooltip link="mailto:${teamInstance.manager.email}" value="${teamInstance.manager}">
                            Click to send email to ${teamInstance.manager.givenName}.
                            <footy:personPhoto person="${teamInstance.manager}"/>
                            <p>Contact ${teamInstance.manager.givenName} on: <strong>${teamInstance.manager.bestPhone().encodeAsHTML()}</strong></p>
                        </footy:tooltip>
                        <tmpl:coachPhotoLink person="${teamInstance.manager}"/>
                    </li>
                    <g:each in="${teamInstance.coaches}" var="c">
                    <li>Coach:
                        <footy:tooltip link="mailto:${c.email}" value="${c}">
                            Click to send email to ${c.givenName}.
                            <footy:personPhoto person="${c}"/>
                            <p>Contact ${c.givenName} on: <strong>${c.bestPhone().encodeAsHTML()}</strong></p>
                        </footy:tooltip>
                        <tmpl:coachPhotoLink person="${c}"/>
                    </li>
                    </g:each>
                </ul>
            </div>

            <div class="newsbox">
                <h2>Calendar</h2>
                <ul>
                    <li>No calendar events yet</li>
                </ul>
            </div>
            
            <div id="otherteams">
                <ul>
                    <g:each in="${Team.findAllByClubAndAgeBand(Club.homeClub, teamInstance.ageBand, [sort:'division'])}" var="t">
                    <g:if test="${t != teamInstance}">
                    <li><g:link action="show" params="${[ageBand:t.ageBand, teamName:t.name]}">U${t.ageBand}&nbsp;${t.name}</g:link></li>
                    </g:if>
                    <g:else>
                    <li><strong>U${t.ageBand}&nbsp;${t.name}</strong></li>
                    </g:else>
                    </g:each>
                </ul>
            </div>

        </div>
    
    </body>
</html>
