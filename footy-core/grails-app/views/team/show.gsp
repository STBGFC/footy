
<%@ page import="org.grails.paypal.PaymentItem; org.grails.paypal.Payment; org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta name="description" content="News, fixtures and information about the ${teamInstance} team"/>
        <feed:meta kind="rss" version="2.0" controller="team" action="feed" id="${teamInstance.id}"/>
        <title>${teamInstance}</title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'bubbletips.css',plugin:'footy-core')}" />
        <g:set var="fb" value="${grailsApplication.config.org?.davisononline?.footy?.usefacebook}"/>
        <g:if test="${fb}">
        <script src="http://connect.facebook.net/en_US/all.js#xfbml=1"></script>
        </g:if>
    </head>
    <body>

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
            </footy:isManager>
            <footy:isNotManager team="${teamInstance}">
            <a
                title="${message(code: 'team.vmail.label', default: 'Email Manager/Coaches')}"
                href="mailto:${[teamInstance.manager, teamInstance.coaches]*.email.flatten().join(",")}"
            >
                <img src="${createLinkTo(dir:'images', file:'vmail.png', plugin:'footy-core')}" alt="${message(code: 'team.vmail.label', default: 'Email Manager/Coaches')}"/>
            </a>
            </footy:isNotManager>

            <!-- address -->
            <g:link action="addresscards" id="${teamInstance.id}" title="${message(code: 'team.vmail.label', default: 'Download contact details for your address book')}">
            <img src="${createLinkTo(dir:'images', file:'vcards.png', plugin:'footy-core')}" alt="${message(code: 'team.vcards.label', default: 'Download Contact Details')}"/>
            </g:link>

            <!-- RSS -->
            <footy:isManager team="${teamInstance}">
            <modalbox:createLink
                  controller="team"
                  action="newsDialog"
                  title="Create News Item"
                  id="${teamInstance.id}"
                  width="420">
                <img src="${createLinkTo(dir:'images', file:'vnews.png', plugin:'footy-core')}" alt="${message(code: 'team.vnews.label', default: 'Add Team News')}"/>
            </modalbox:createLink>
            </footy:isManager>
            <g:link action="feed" id="${teamInstance.id}" title="${message(code: 'team.feed.label', default: 'Subscribe to news feed for ' + teamInstance.toString())}">
            <img src="${createLinkTo(dir:'images', file:'rss.png', plugin:'footy-core')}" alt="${message(code: 'team.feed.label', default: 'Subscribe')}"/>
            </g:link>
        </div>

        <div id="homemain">

            <div id="sponsor">
                <p>
                    ${teamInstance.sponsor ? teamInstance.toString() + " are proud to be sponsored by" : "No sponsor identified for this team"}
                </p>
                <footy:sponsorLogo sponsor="${teamInstance?.sponsor}"/>
                <footy:isManager team="${teamInstance}">
                <p>
                <modalbox:createLink
                        controller="team"
                        action="selectSponsorDialog"
                        id="${teamInstance.id}"
                        title="Team Sponsor"
                        width="330">
                    <g:message code="org.davisononline.footy.core.sponsoreditlabel" default="Add/Change sponsor"/>
                </modalbox:createLink>
                </p>
                </footy:isManager>
            </div>

            <g:if test="${teamInstance?.division}">
            <div id="leagueTable">
                <h2>${teamInstance.division}</h2>
                <footy:fullTimeLeagueTable division="${teamInstance?.division}"/>
            </div>
            </g:if>

            <g:if test="${latestNews.size() != 0}">
                <g:each in="${latestNews}" var="news">
                    <g:set var="abst" value="${news.abstractText()}"/>
                    <h2>${news.subject.encodeAsHTML()}</h2>
                    <p class="byline"><g:formatDate date="${news.createdDate}" format="dd MMM 'at' HH:mm"/></p>
                    <p id="abstractNewsBody${news.id}" class="newsBody">
                        ${abst.encodeAsHTML()}
                        <g:if test="${abst.endsWith(' ...')}">
                        <a href="#"
                           title="Read the full article"
                           onclick="$('abstractNewsBody${news.id}').hide();Effect.BlindDown('fullNewsBody${news.id}', { duration: 0.5 });return false">
                            <g:message code="org.davisononline.footy.core.team.readmore.label" default="&gt;&gt;"/>
                        </a>
                        </g:if>
                    </p>
                    <g:if test="${abst.endsWith(' ...')}">
                    <p class="completeArticle" style="display:none" id="fullNewsBody${news.id}">${news.body.encodeAsHTML().replace('\n\n', '<br/><br/>').replace('\r\n\r\n', '<br/><br/>').replace('\r\r', '<br/><br/>')}</p>
                    </g:if>
                </g:each>
                <g:if test="${!params.maxNews}">
                <p>
                    <g:link controller="team" action="show" params="${[ageBand:teamInstance.ageBand, teamName:teamInstance.name, maxNews: 100]}">
                    <g:message code="org.davisononline.footy.match.readall.link" default="Read All News..."/>
                    </g:link>
                </p>
                </g:if>
            </g:if>

            <footy:isManager team="${teamInstance}">
            <h2 style="clear: both; margin-top: 40px;">Squad (${teamInstance.players.size()} Players)</h2>
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

            <%-- obligatory facebook stuff --%>
            <g:if test="${fb}">
            <div class="fb-like" data-href="${grailsApplication.config.grails.serverURL}/u${teamInstance.ageBand}/${teamInstance.name}" style="margin-top:50px;"></div>
            </g:if>
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
                    <li>Manager:
                        <footy:tooltip link="mailto:${teamInstance.manager.email}" value="${teamInstance.manager}">
                            Click to send email to ${teamInstance.manager.givenName}.
                            <footy:personPhoto person="${teamInstance.manager}"/>
                            <p>Contact ${teamInstance.manager.givenName} on: <strong>${teamInstance.manager.bestPhone().encodeAsHTML()}</strong></p>
                        </footy:tooltip>
                        <g:render template="coachPhotoLink" model="[person:teamInstance.manager]"/>
                    </li>
                    <g:each in="${teamInstance.coaches}" var="c">
                    <li>Coach:
                        <footy:tooltip link="mailto:${c.email}" value="${c}">
                            Click to send email to ${c.givenName}.
                            <footy:personPhoto person="${c}"/>
                            <p>Contact ${c.givenName} on: <strong>${c.bestPhone().encodeAsHTML()}</strong></p>
                        </footy:tooltip>
                        <g:render template="coachPhotoLink" model="[person:c]"/>
                    </li>
                    </g:each>
                    <footy:isNotManager team="${teamInstance}">
                    <li>Players:<br/>
                        ${players.join(", ")}
                    </li>
                    </footy:isNotManager>
                </ul>
            </div>

            <div id="fixtureList">
            <g:render template="/fixture/fixtureList" plugin="footy-match" model="${fixtures}"/>
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
