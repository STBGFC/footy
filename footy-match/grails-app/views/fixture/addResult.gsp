<%@ page import="org.davisononline.footy.match.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>
            ${fixtureInstance.team}
            <g:message code="org.davisononline.footy.match.addresult.title" default="Result" />
            (<g:formatDate date="${fixtureInstance.dateTime}" format="dd/MM/yyyy"/>)
        </title>
        <g:javascript>
            function toggleQuestions(sel) {
                var noref = (sel[sel.selectedIndex].value == "null")
                if (noref) {
                    for (var i=0; i<10; i++) {
                        Effect.Fade('rowQ' + i)
                    }
                    Effect.Fade('rowQcomments')
                }
                else {
                    for (var i=0; i<10; i++) {
                        Effect.Appear('rowQ' + i)
                    }
                    Effect.Appear('rowQcomments')
                }
            }
        </g:javascript>
    </head>
    <body>
        <div class="dialog">
            <h2>
                Score
            </h2>

            <g:form method="post"  enctype="multipart/form-data">
                <g:hiddenField name="id" value="${fixtureInstance?.id}"/>
                <g:hiddenField name="version" value="${fixtureInstance?.version}"/>
                <g:if test="${minAge > fixtureInstance.team.ageBand}">
                    <p>Teams below U${minAge} cannot publish results</p>
                </g:if>
                <g:else>
                <div class="dialog">
                    <table>
                        <tbody>
                            <tr class="prop">
                                <td valign="top" class="name">&nbsp;</td>
                                <td class="title right date">
                                    <g:if test="${fixtureInstance.homeGame}">${fixtureInstance.team.club} ${fixtureInstance.team.name}</g:if>
                                    <g:else>${fixtureInstance.opposition}</g:else>
                                </td>
                                <td class="title">-</td>
                                <td class="title date">
                                    <g:if test="${!fixtureInstance.homeGame}">${fixtureInstance.team.club} ${fixtureInstance.team.name}</g:if>
                                    <g:else>${fixtureInstance.opposition}</g:else>
                                </td>
                            </tr>
                            <tr class="prop">
                                <td valign="top" class="name">Full-time score</td>
                                <td class="title date right">
                                    <g:textField name="homeGoalsFullTime" class="goalInput" value="${fixtureInstance.homeGoalsFullTime}"/>
                                </td>
                                <td class="title">-</td>
                                <td class="title date">
                                    <g:textField name="awayGoalsFullTime" class="goalInput" value="${fixtureInstance.awayGoalsFullTime}"/>
                                </td>
                            </tr>
                            <tr class="prop">
                                <td valign="top" class="name">Half-time score</td>
                                <td class="date right">
                                    <g:textField name="homeGoalsHalfTime" class="goalInput" value="${fixtureInstance.homeGoalsHalfTime}"/>
                                </td>
                                <td>-</td>
                                <td class="date">
                                    <g:textField name="awayGoalsHalfTime" class="goalInput" value="${fixtureInstance.awayGoalsHalfTime}"/>
                                </td>
                            </tr>
                            <g:if test="${fixtureInstance.type == Fixture.CUP_GAME}">
                            <tr class="prop">
                                <td valign="top" class="name">Score after extra time (if played)</td>
                                <td class="date right">
                                    <g:textField
                                            name="homeGoalsExtraTime"
                                            class="goalInput"
                                            value="${fixtureInstance.extraTime ? fixtureInstance.homeGoalsExtraTime : ''}"/>
                                </td>
                                <td>-</td>
                                <td class="date">
                                    <g:textField
                                            name="awayGoalsExtraTime"
                                            class="goalInput"
                                            value="${fixtureInstance.extraTime ? fixtureInstance.awayGoalsExtraTime : ''}"/>
                                </td>
                            </tr>
                            <tr class="prop">
                                <td valign="top" class="name">Score after penalties (if needed)</td>
                                <td class="date right">
                                    <g:textField
                                            name="homeGoalsPenalties"
                                            class="goalInput"
                                            value="${fixtureInstance.penalties ? fixtureInstance.homeGoalsPenalties : ''}"/>
                                </td>
                                <td>-</td>
                                <td class="date">
                                    <g:textField
                                            name="awayGoalsPenalties"
                                            class="goalInput"
                                            value="${fixtureInstance.penalties ? fixtureInstance.awayGoalsPenalties : ''}"/>
                                </td>
                            </tr>
                            </g:if>
                            <tr class="prop">
                                <td valign="top" class="name">Match Report (can be added later if you wish)</td>
                                <td colspan="3">
                                    <g:textArea cols="80" rows="40" name="matchReport">${fixtureInstance.matchReport}</g:textArea>
                                </td>
                            </tr>

                        </tbody>
                    </table>
                </div>
                </g:else>

                <g:if test="${includeRefReport}">
                <h2>Referee Report</h2>
                <g:if test="${fixtureInstance.homeGame && !refReportExists}">
                <p>
                    Once submitted, the referee report cannot be amended.  Please answer <strong>ALL</strong>
                    multiple choice questions
                </p>
                <div class="dialog">
                    <table>
                        <tbody>
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <g:message code="org.davisononline.footy.match.label.referee" default="Referee" />
                                </td>
                                <td class="value">
                                    <g:select
                                            name="ref.referee.id"
                                            from="${availableReferees}"
                                            value="${fixtureInstance.referee?.id}"
                                            noSelection="[null:'-- Unlisted ref / No-one --']"
                                            onchange="toggleQuestions(this)"
                                            optionKey="id"/>
                                </td>
                            </tr>
                            <%-- question template should be provided by the application, not the plugin as the question/answer format is variable --%>
                            <g:render template="/editRefReport"/>
                        </tbody>
                    </table>
                </div>
                <g:javascript>toggleQuestions($('ref.referee.id'))</g:javascript>
                </g:if>
                <g:if test="${!fixtureInstance.homeGame}">
                    <p>Referee report not required for this fixture</p>
                </g:if>
                <g:if test="${fixtureInstance.homeGame && refReportExists}">
                    <p>Referee report already submitted for this fixture</p>
                </g:if>
                </g:if>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="saveResult" value="${message(code: 'default.button.save.label', default: 'Save')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
