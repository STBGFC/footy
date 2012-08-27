
<%@ page import="org.davisononline.footy.match.Fixture" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>
            <g:message code="org.davisononline.footy.match.views.fixture.list.para1" default="Results and fixtures for "/>
            ${myteam}
        </title>
    </head>
    <body>

        <div class="main" id="main-two-columns">

			<div class="left" id="main-left">

                <g:each in="${fixtures.grep {it.played}}" var="fixture" status="i">
                <g:if test="${i==0}">
				<div class="post">

					<div class="post-title">
                        <h2>
                            ${fixture}
                            <footy:isManager team="${myteam}">
                            <g:link
                                    controller="fixture"
                                    action="addResult"
                                    id="${fixture.id}"
                                    title="Add result/report"
                                    width="450">
                                <r:img dir="images" file="edit.png" plugin="footy-core" title="Edit this fixture or add a report"/>
                            </g:link>
                            </footy:isManager>
                        </h2>
                    </div>

					<div class="post-date">
                        <g:formatDate date="${fixture.dateTime}" format="dd MMMM yyyy 'at' HH:mm"/>
                        :: ${fixture.type}
                        <g:if test="${fixture.type == Fixture.LEAGUE_GAME}">
                             :: ${fixture.team.division}
                        </g:if>
					</div>

					<div class="post-body">
                        <g:if test="${fixture.matchReport?.length() > 0}">
                        ${fixture.matchReport.encodeAsHTML().replace('\n\n', '<br/><br/>').replace('\r\n\r\n', '<br/><br/>').replace('\r\r', '<br/><br/>')}
                        </g:if>
                        <g:else>
                        <g:message code="org.davisononline.footy.match.views.fixture.noreport" default="No match report added for this game"/>
                        </g:else>

					    <div class="clearer">&nbsp;</div>
					</div>

				</div>
				<div class="content-separator"></div>
                </g:if>

                <g:else>
				<div class="post">

					<h4>
                        ${fixture}
                        <footy:isManager team="${myteam}">
                        <g:link
                                controller="fixture"
                                action="addResult"
                                id="${fixture.id}"
                                title="Add result/report"
                                width="450">
                            <r:img dir="images" file="edit.png" plugin="footy-core" title="Edit this fixture or add a report"/>
                        </g:link>
                        </footy:isManager>
                    </h4>
					<div class="post-date">
                        <g:formatDate date="${fixture.dateTime}" format="dd MMMM yyyy 'at' HH:mm"/>
                        :: ${fixture.type}
                        <g:if test="${fixture.type == Fixture.LEAGUE_GAME}">
                             :: ${fixture.team.division}
                        </g:if>
					</div>

                    <g:if test="${fixture.matchReport?.length() > 0}">
                    <a
                        id="reportTrigger${fixture.id}"
                        href="#"
                        onclick="$('reportTrigger${fixture.id}').hide();Effect.BlindDown('matchReport${fixture.id}', {duration: 0.5});return false;">
                        <g:message code="org.davisononline.footy.match.viewreport.link" default="View Report"/> &#187;
                    </a>
                    <p id="matchReport${fixture.id}" style="display:none">
                        ${fixture.matchReport.encodeAsHTML().replace('\n\n', '<br/><br/>').replace('\r\n\r\n', '<br/><br/>').replace('\r\r', '<br/><br/>')}
                    </p>
                    </g:if>

					<div class="clearer">&nbsp;</div>

				</div>

				<div class="content-separator"></div>
                </g:else>
                </g:each>
                
                <p>
                    <g:link controller="team" action="show" params="${[ageBand:myteam.ageBand, teamName:myteam.name]}">
                        <g:message code="org.davisononline.footy.match.label.returntoteam" default="Back to team page"/>
                    </g:link>
                </p>

				<div class="content-separator"></div>
			</div>

			<div class="right sidebar" id="sidebar">

				<div class="section" id="fixtureList">

					<g:render template="fixtureList" model="${[fixtures:fixtures.grep {!it.played}.reverse()]}" />

				</div>

            </div>

			<div class="clearer">&nbsp;</div>

        </div>
    </body>
</html>
