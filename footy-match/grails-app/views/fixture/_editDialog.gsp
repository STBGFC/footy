<%@ page import="org.davisononline.footy.match.Fixture" %>
<g:form name="editFixture">
    <g:hiddenField name="id" value="${fixtureInstance?.id}"/>
    <g:hiddenField name="version" value="${fixtureInstance?.version}"/>
    <style type="text/css">
        td.title {font-size: large}
        td.title input {font-size: large; font-weight: bold}
        td.right {text-align: right}
        input.goalInput {width: 20px; text-align: center;}
        textarea {width: 290px; height: 190px}
    </style>
    <table>
        <tr>
            <td>&nbsp;</td>
            <td class="title right">
                <g:if test="${fixtureInstance.homeGame}">${fixtureInstance.team.club} ${fixtureInstance.team.name}</g:if>
                <g:else>${fixtureInstance.opposition}</g:else>
            </td>
            <td class="title">-</td>
            <td class="title">
                <g:if test="${!fixtureInstance.homeGame}">${fixtureInstance.team.club} ${fixtureInstance.team.name}</g:if>
                <g:else>${fixtureInstance.opposition}</g:else>
            </td>
        </tr>
        <tr>
            <td>Full-time score</td>
            <td class="title right">
                <g:textField name="homeGoalsFullTime" class="goalInput" value="${fixtureInstance.homeGoalsFullTime}"/>
            </td>
            <td cass="title">-</td>
            <td class="title">
                <g:textField name="awayGoalsFullTime" class="goalInput" value="${fixtureInstance.awayGoalsFullTime}"/>
            </td>
        </tr>
        <tr>
            <td>Half-time score</td>
            <td class="right">
                <g:textField name="homeGoalsHalfTime" class="goalInput" value="${fixtureInstance.homeGoalsHalfTime}"/>
            </td>
            <td>-</td>
            <td>
                <g:textField name="awayGoalsHalfTime" class="goalInput" value="${fixtureInstance.awayGoalsHalfTime}"/>
            </td>
        </tr>
        <g:if test="${fixtureInstance.type == Fixture.CUP_GAME}">
        <tr>
            <td>Score after extra time (if played)</td>
            <td class="right">
                <g:textField name="homeGoalsExtraTime" class="goalInput" value="${fixtureInstance.extraTime ? fixtureInstance.homeGoalsExtraTime : ''}"/>
            </td>
            <td>-</td>
            <td>
                <g:textField name="awayGoalsExtraTime" class="goalInput" value="${fixtureInstance.extraTime ? fixtureInstance.awayGoalsExtraTime : ''}"/>
            </td>
        </tr>
        <tr>
            <td>Score after penalties (if needed)</td>
            <td class="right">
                <g:textField name="homeGoalsPenalties" class="goalInput" value="${fixtureInstance.penalties ? fixtureInstance.homeGoalsPenalties : ''}"/>
            </td>
            <td>-</td>
            <td>
                <g:textField name="awayGoalsPenalties" class="goalInput" value="${fixtureInstance.penalties ? fixtureInstance.awayGoalsPenalties : ''}"/>
            </td>
        </tr>
        </g:if>
        <tr>
            <td>Match Report (can be added later if you wish)</td>
            <td colspan="3">
                <g:textArea cols="80" rows="40" name="matchReport">
                    ${fixtureInstance.matchReport}
                </g:textArea>
            </td>
        </tr>
    </table>
    <g:submitToRemote
        value="Save"
        title="Save details"
        onFailure="alert('Failed to save, check all data')"
        onSuccess="Modalbox.hide()"
        update="fixtureList"
        url="[controller:'fixture',action:'saveResult']"
        />
</g:form>
