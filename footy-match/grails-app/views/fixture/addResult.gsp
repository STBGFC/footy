<%@ page import="org.davisononline.footy.match.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>
            ${fixtureInstance.team}
            <g:message code="org.davisononline.footy.match.addresult.title" default="Result" />
            (<g:formatDate date="${fixtureInstance.dateTime}" format="dd/MM/yyyy"/>)
        </title>
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
                    <g:render template="/refReport"/>
                </g:if>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="saveResult" value="${message(code: 'default.button.save.label', default: 'Save')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
