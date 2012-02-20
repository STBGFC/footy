
<%@ page import="org.davisononline.footy.match.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>
            <g:message code="org.davisononline.footy.match.views.fixture.refreports.title" default="Referee Report Summary:"/>
            <g:formatDate date="${date}" format="dd MMM yyyy"/>
        </title>
        <g:javascript>
            function changeDate() {
                document.location = '<g:createLink controller="resource" action="reports"/>/' + $('date_year').value + '/' + $('date_month').value + '/' + $('date_day').value
            }
        </g:javascript>
    </head>
    <body>
        <div class="list">
            <g:if test="${reports.size() == 0}">
            <p>
                <g:message code="org.davisononline.footy.match.nofixtures.text" default="No reports found for this date"/>
            </p>
            </g:if>
            <div class="nav date">
                Select a different date: <g:datePicker name="date" precision="day" years="${(date.year-1 + 1900)..(date.year+1901)}" value="${date}"/>
                <input type="submit" value="Change Date" onclick="changeDate()"/>
            </div>
            <g:if test="${reports.size() > 0}">
            <table id="reportSummary">
                <thead>
                    <tr>
                        <th>
                            <g:message code="org.davisononline.footy.match.label.fixture" default="Fixture" />
                        </th>
                        <th>
                            <g:message code="org.davisononline.footy.match.label.referee" default="Referee" />
                        </th>
                        <th>
                            <g:message code="org.davisononline.footy.match.label.refscore" default="Score" />
                        </th>
                        <th style="width:300px">
                            <g:message code="org.davisononline.footy.match.label.refcomment" default="Manager's Comments" />
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${reports}" var="report" status="c">
                    <tr class="${c%2 == 0 ?'odd' : 'even'}">
                        <td>
                            <p>
                                <g:link title="View Details" action="refReport" id="${report.id}">${report.fixture}</g:link>
                            </p>
                            <g:if test="${report.fixture.team?.manager}">
                            Manager: ${report.fixture.team.manager} (${report.fixture.team.manager.bestPhone()})
                            </g:if>
                            <g:else><em>No manager listed for this team</em></g:else>
                        </td>
                        <td>
                            <g:if test="${report.referee}">
                            ${report.referee}
                            </g:if>
                            <g:else><em>Not Assigned</em></g:else>
                        </td>
                        <td>
                            ${report.total}
                        </td>
                        <td>
                            ${report.comments}
                        </td>
                    </tr>
                    </g:each>
                </tbody>
            </table>
            </g:if>
        </div>
    </body>
</html>
