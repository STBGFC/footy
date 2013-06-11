
<%@ page import="org.davisononline.footy.match.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>
            <g:message code="org.davisononline.footy.match.views.fixture.refreport.title" default="Referee Report"/>
        </title>
    </head>
    <body>
        <div class="list">
            <g:if test="${!report}">
            <p>
                <g:message code="org.davisononline.footy.match.noreport.text" default="No report found"/>
            </p>
            </g:if>
            <g:else>
            <h2>
                <g:message code="org.davisononline.footy.match.report.headertext" default="Referee report for"/>
                ${report.referee} (${report.fixture})
            </h2>

            <table id="reportSummary">
                <thead>
                    <tr>
                        <th>
                            <g:message code="org.davisononline.footy.match.label.fixture" default="Question" />
                        </th>
                        <th>
                            <g:message code="org.davisononline.footy.match.label.refscore" default="Score" />
                        </th>
                    </tr>
                </thead>
                <tbody>
                <g:each in="${0..totQuestions-1}" var="c">
                    <tr class="${c%2 == 0 ?'odd' : 'even'}">
                        <td>
                            <g:message code="org.davisononline.footy.match.refreport.q${c}" default="Q${c}" />
                        </td>
                        <td>
                            ${report.scores[c]}
                        </td>
                    </tr>
                </g:each>
                    <tr class="${totQuestions%2 == 0 ?'odd' : 'even'}">
                        <td>
                            <g:message code="org.davisononline.footy.match.refreport.showcomments" default="Manager Comments" />
                        </td>
                        <td>
                            ${report.comments}
                        </td>
                    </tr>
                </tbody>
            </table>

            </g:else>
        </div>
    </body>
</html>
