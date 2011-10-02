
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
        <div>
            <p>
                <g:link controller="team" action="show" params="${[ageBand:myteam.ageBand, teamName:myteam.name]}">
                    <g:message code="org.davisononline.footy.match.label.returntoteam" default="Back to team page"/>
                </g:link>
            </p>

            <table>
                <tbody>
                <g:each in="${fixtures}" var="fixture">
                    <tr>
                        <td class="fixtureList">
                            <div>
                                <footy:isManager team="${myteam}">
                                    <modalbox:createLink
                                        controller="fixture"
                                        action="editDialog"
                                        id="${fixture.id}"
                                        title="Edit result or add match report"
                                        width="450">
                                        <img
                                            src="${createLinkTo(dir:'images/skin', file:'database_edit.png', plugin: 'footy-core')}"
                                            alt="edit"/>
                                    </modalbox:createLink>
                                </footy:isManager>
                                <g:formatDate date="${fixture.dateTime}" format="dd MMMM yyyy"/>
                                :: ${fixture.type}
                                <g:if test="${fixture.type == Fixture.LEAGUE_GAME}">
                                     :: ${fixture.team.league} Div. ${fixture.team.division}
                                </g:if>
                            </div>

                            <span class="fixtureListResult">${fixture}</span>
                            <g:if test="${fixture.matchReport?.length() > 0}">
                                <a
                                    id="reportTrigger${fixture.id}"
                                    href="#"
                                    onclick="$('reportTrigger${fixture.id}').hide();Effect.BlindDown('matchReport${fixture.id}', {duration: 0.5});return false;">
                                    <g:message code="org.davisononline.footy.match.viewreport.link" default="View Report"/>
                                </a>
                                <p id="matchReport${fixture.id}" style="display:none">
                                    ${fixture.matchReport.encodeAsHTML().replace('\n\n', '<br/><br/>')}
                                </p>
                            </g:if>
                        </td>
                    </tr>
                </g:each>
                </tbody>
            </table>
        </div>
    </body>
</html>
