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
                <g:radioGroup name="homeGame" values="${['true','false']}" labels="${['home','away']}" value="true">
                    ${it.radio} ${it.label}
                </g:radioGroup>
            </td>
        </tr>
        <tr>
            <td>Opposition (full name)</td>
            <td>
                <%-- // leave off for now.. confusing.
                <g:select 
                        name="opposition" 
                        from="${oppositionTeams}" 
                        optionValue="${{it.club.name + ' ' + it.toString()}}"
                        optionKey="id"
                        noSelection="${['-1':'-- Not Listed --']}"
                />
                <br/>
                If your opposition is not listed in the drop-down above, simply write their team name in the box below instead (ensure you 
                leave the drop-down set to "-- Not Listed --"
                <br/>
                --%>
                <g:hiddenField name="opposition" value="-1"/>
                <g:textField name="opposition2"/>
            </td>
        </tr>
        <tr>
            <td>Date/Time</td>
            <td class="date">
                <g:set var="now" value="${new Date()}"/>
                <g:datePicker name="dateTime" precision="minute" years="${now.year+1899..now.year+1901}"/>
                <p>(For home games, select your PREFERRED kick off time)</p>
            </td>
        </tr>
    </table>
    <g:hiddenField name="team.id" value="${teamInstance.id}" />
    <g:submitToRemote 
        value="Create" 
        title="Create" 
        update="fixtureList"
        onSuccess="alert('Fixture saved')"
        onFailure="alert('Failed to save!  Check values')"
        url="[controller:'fixture',action:'save']"
        />
</g:form>
