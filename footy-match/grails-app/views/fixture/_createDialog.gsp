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
                <input type="hidden" name="dateTime" value="date.struct">
                <select name="dateTime_day" id="dateTime_day" class="MB_focusable">
                    <g:each in="${1..31}" var="day">
                    <option value="${day}">${day}</option>
                    </g:each>
                </select>
                <select name="dateTime_month" id="dateTime_month" class="MB_focusable">
                    <option value="1">January</option>
                    <option value="2">February</option>
                    <option value="3">March</option>
                    <option value="4">April</option>
                    <option value="5">May</option>
                    <option value="6">June</option>
                    <option value="7">July</option>
                    <option value="8" selected="selected">August</option>
                    <option value="9">September</option>
                    <option value="10">October</option>
                    <option value="11">November</option>
                    <option value="12">December</option>
                </select>
                <select name="dateTime_year" id="dateTime_year" class="MB_focusable">
                    <option value="${now.year+1899}">${now.year+1899}</option>
                    <option value="${now.year+1900}" selected="selected">${now.year+1900}</option>
                    <option value="${now.year+1901}">${now.year+1901}</option>
                </select>
                <br/>
                <select name="dateTime_hour" id="dateTime_hour" class="MB_focusable">
                    <g:each in="${7..21}" var="hour">
                    <option value="<g:formatNumber number="${hour}" format="00"/>"><g:formatNumber number="${hour}" format="00"/></option>
                    </g:each>
                </select> :
                <select name="dateTime_minute" id="dateTime_minute" class="MB_focusable">
                    <option value="00" selected="selected">00</option>
                    <option value="15">15</option>
                    <option value="30">30</option>
                    <option value="45">45</option>
                </select>

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
