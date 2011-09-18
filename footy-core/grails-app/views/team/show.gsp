
<%@ page import="org.grails.paypal.PaymentItem; org.grails.paypal.Payment; org.davisononline.footy.core.*" %>
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

                <footy:isManager team="${teamInstance}">
                <g:if test="${teamInstance?.id && players.size() > 0}">
                <g:link action="leagueForm" id="${teamInstance?.id}" title="${message(code: 'team.vprint.label', default: 'Print registration form')}">
                <img src="${createLinkTo(dir:'images', file:'vprint.png', plugin:'footy-core')}" alt="${message(code: 'team.vprint.label', default: 'Print registration form')}"/>
                </g:link>
                </g:if>

                <modalbox:createLink
                      controller="team"
                      action="messageDialog"
                      title="Send Email Message"
                      id="${teamInstance.id}"
                      width="420">
                    <img src="${createLinkTo(dir:'images', file:'vmail.png', plugin:'footy-core')}" alt="${message(code: 'team.vmail.label', default: 'Send Email')}"/>
                </modalbox:createLink>
                <modalbox:createLink
                      controller="team"
                      action="newsDialog"
                      title="Create News Item"
                      id="${teamInstance.id}"
                      width="420">
                    <img src="${createLinkTo(dir:'images', file:'vnews.png', plugin:'footy-core')}" alt="${message(code: 'team.vnews.label', default: 'Add Team News')}"/>
                </modalbox:createLink>
                </footy:isManager>

                <footy:isNotManager team="${teamInstance}">
                <a
                    title="${message(code: 'team.vmail.label', default: 'Email Manager/Coaches')}"
                    href="mailto:${[teamInstance.manager, teamInstance.coaches]*.email.flatten().join(",")}"
                >
                    <img src="${createLinkTo(dir:'images', file:'vmail.png', plugin:'footy-core')}" alt="${message(code: 'team.vmail.label', default: 'Email Manager/Coaches')}"/>
                </a>
                </footy:isNotManager>

                <g:link action="addresscards" id="${teamInstance.id}" title="${message(code: 'team.vmail.label', default: 'Download contact details for your address book')}">
                <img src="${createLinkTo(dir:'images', file:'vcards.png', plugin:'footy-core')}" alt="${message(code: 'team.vcards.label', default: 'Download Contact Details')}"/>
                </g:link>
            </div>

            <g:if test="${latestNews.size() == 0}">
            <h2><g:message code="org.davisononline.footy.core.team.news.title" default="Latest News &amp; Results"/></h2>
            <p>
                <g:message code="org.davisononline.footy.core.team.nonews.test" default="No news yet."/>
            </p>
            </g:if>
            <g:else>
                <g:each in="${latestNews}" var="news">
                    <g:set var="abst" value="${news.abstractText()}"/>
                    <h2><g:formatDate date="${news.createdDate}" format="dd MMM"/>: ${news.subject.encodeAsHTML()}</h2>
                    <p id="abstractNewsBody${news.id}" class="newsBody">
                        ${abst}
                        <g:if test="${abst.endsWith(' ...')}">
                        <br/><a href="#" onclick="$('abstractNewsBody${news.id}').hide();Effect.BlindDown('fullNewsBody${news.id}', { duration: 0.5 })"><g:message code="org.davisononline.footy.core.team.readmore.label" default="Read More..."/></a>
                        </g:if>
                    </p>
                    <g:if test="${abst.endsWith(' ...')}">
                    <p class="completeArticle" style="display:none" id="fullNewsBody${news.id}">${news.body.encodeAsHTML()}</p>
                    </g:if>
                </g:each>
                <g:if test="${!params.maxNews}">
                <p style="text-align:right">
                    <g:link controller="team" action="show" params="${[ageBand:teamInstance.ageBand, teamName:teamInstance.name, maxNews: 100]}">
                    <g:message code="org.davisononline.footy.match.readall.link" default="Read All"/>
                    </g:link>
                </p>
                </g:if>
            </g:else>

            <footy:isManager team="${teamInstance}">
            <h2>Squad (${teamInstance.players.size()} Players)</h2>
            <table class="list">
                <thead>
                    <tr>
                        <th>${message(code: 'person.name.label', default: 'Name')} (${message(code: 'player.dateOfBirth.label', default: 'DoB')})</th>
                        <th>${message(code: 'player.medical.label', default: 'Medical')}</th>
                        <th>${message(code: 'player.contactDetails.label', default: 'Contact')}</th>
                        <th>${message(code: 'player.leagueRegistrationNumber.label', default: 'League#')}</th>
                        <th>${message(code: 'player.currentRegistrationStatus.label', default: 'Payment')}</th>
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
                        <td>
                            <footy:paymentStatus payment="${PaymentItem.findByItemNumber(player?.currentRegistration?.id)?.payment}"/>
                        </td>
                    </tr>
                </g:each>
                </tbody>
            </table>
            </footy:isManager>
        </div>


        <div id="newspanel">
            <%-- have to do this 2/3 times to get the right output for the requirements.
             1. is manager.. --%>
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
            <%-- 2. EDITOR but not manager can also do this.. --%>
            <sec:ifAnyGranted roles="ROLE_EDITOR">
            <footy:isNotManager team="${teamInstance}">
            <modalbox:createLink
                    controller="team"
                    action="photoUploadDialog"
                    id="${teamInstance.id}"
                    title="Team Photo Upload"
                    width="400">
                <footy:teamPhoto team="${teamInstance}"/>
            </modalbox:createLink>
            </footy:isNotManager>
            </sec:ifAnyGranted>
            <%-- 3. neither EDITOR nor manager --%>
            <footy:isNotManager team="${teamInstance}">
            <sec:ifNotGranted roles="ROLE_EDITOR">
            <footy:teamPhoto team="${teamInstance}"/>
            </sec:ifNotGranted>
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
                        <g:render template="coachPhotoLink" model="[person:teamInstance.manager]" plugin="footy-core"/>
                    </li>
                    <g:each in="${teamInstance.coaches}" var="c">
                    <li>Coach:
                        <footy:tooltip link="mailto:${c.email}" value="${c}">
                            Click to send email to ${c.givenName}.
                            <footy:personPhoto person="${c}"/>
                            <p>Contact ${c.givenName} on: <strong>${c.bestPhone().encodeAsHTML()}</strong></p>
                        </footy:tooltip>
                        <g:render template="coachPhotoLink" model="[person:c]" plugin="footy-core"/>
                    </li>
                    </g:each>
                    <footy:isNotManager team="${teamInstance}">
                    <li>Players:<br/>
                        ${players.join(", ")}
                    </li>
                    </footy:isNotManager>
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
