<%@ page import="org.davisononline.footy.match.Fixture" %>
<g:form name="addFixture">
    <table>
        <tr>
            <td>Type of game</td>
            <td>
                <g:radioGroup name="type" values="${Fixture.GAME_TYPES}" labels="${Fixture.GAME_TYPES}" value="${Fixture.LEAGUE_GAME}">
                    ${it.radio} ${it.label}
                </g:radioGroup>
            </td>
        </tr>
        <tr>
            <td>Venue</td>
            <td>
                <g:radioGroup name="venue" values="${['home','away']}" labels="${['home','away']}" value="home">
                    ${it.radio} ${it.label}
                </g:radioGroup>
            </td>
        </tr>
        <tr>
            <td>Opposition</td>
            <td>
                <g:select name="awayTeam.id" optionKey="id" from="${oppositionTeams}" optionValue="${{it.club.name + ' ' + it}}"/>
                <g:link controller="team" action="create"><g:message code="org.davisononline.footy.match.newteam" default="Add Team"/></g:link>
            </td>
        </tr>
        <tr>
            <td>Date/Time</td>
            <td class="date">
                <g:set var="now" value="${new Date()}"/>
                <g:datePicker name="dateTime" precision="minute" years="${now.year+1899..now.year+1901}"/>
            </td>
        </tr>
    </table>
    <g:hiddenField name="homeTeam.id" value="${teamInstance.id}" />
    <g:submitToRemote value="Create" title="Create" update="fixtureList" url="[controller:'fixture',action:'save']"/>
</g:form>