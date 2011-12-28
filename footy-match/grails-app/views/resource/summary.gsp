
<%@ page import="org.davisononline.footy.match.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>
            <g:message code="org.davisononline.footy.match.views.fixture.summary.title" default="Pitch Allocations:"/>
            <g:formatDate date="${date}" format="dd MMM yyyy"/>
        </title>
        <g:javascript>
            function changeDate() {
                document.location = '<g:createLink controller="resource" action="summary"/>/' + $('date_year').value + '/' + $('date_month').value + '/' + $('date_day').value
            }
        </g:javascript>
    </head>
    <body>
        <div class="list">
            <g:if test="${fixtures.size() == 0}">
            <p>
                <g:message code="org.davisononline.footy.match.nofixtures.text" default="No fixtures found for this date"/>
            </p>
            </g:if>
            <div class="nav date">
                Select a different date: <g:datePicker name="date" precision="day" years="${(date.year-1 + 1900)..(date.year+1901)}" value="${date}"/>
                <input type="submit" value="Change Date" onclick="changeDate()"/>
            </div>
            <g:if test="${fixtures.size() > 0}">
            <table id="fixtureSummary">
                <thead>
                    <tr>
                        <th>
                            &nbsp;
                        </th>
                        <th>
                            <g:message code="org.davisononline.footy.match.label.fixture" default="Fixture" />
                        </th>
                        <th>
                            <g:message code="org.davisononline.footy.match.label.referee" default="Referee" />
                        </th>
                        <th>
                            <g:message code="org.davisononline.footy.match.label.other" default="Other" />
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${fixtures*.resources*.find{it.type == MatchResource.PITCH}.unique().sort()}" var="pitch" status="c">
                    <g:set var="perPitch" value="${fixtures.findAll {it.resources.contains(pitch)}}"/>
                    <g:each in="${perPitch}" var="fixture" status="n">
                    <tr class="${c%2 == 0 ?'odd' : 'even'}">
                        <g:if test="${n==0}">
                        <td rowspan="${perPitch.size()}">
                            ${pitch}
                        </td>
                        </g:if>
                        <td>
                            <p style="font-size:larger;font-weight: bold;margin-bottom: 0px"><g:formatDate date="${fixture.dateTime}" format="HH:mm"/>: ${fixture}</p>
                            <g:if test="${fixture.team?.manager}">
                            Contact: ${fixture.team.manager} (${fixture.team.manager.bestPhone()})<br/>
                            <a href="mailto:${fixture.team.manager.email}">${fixture.team.manager.email}</a>
                            </g:if>
                            <g:else><em>No manager listed for this team</em></g:else>
                        </td>
                        <td>
                            <g:if test="${fixture.referee}">
                            ${fixture.referee} (${fixture.referee.bestPhone()})<br/>
                            <a href="mailto:${fixture.referee.email}">${fixture.referee.email}</a>
                            </g:if>
                            <g:else><em>Not Assigned</em></g:else>
                        </td>
                        <td>
                            <g:set var="other" value="${fixture.resources.findAll{it.type != MatchResource.PITCH}}"/>
                            ${other.join(',')}
                        </td>
                    </tr>
                    </g:each>
                    </g:each>
                </tbody>
            </table>
            </g:if>
        </div>
    </body>
</html>
