            <g:if test="${fixtures.size()>0}">
            <g:form method="post" action="commitAllocations">

                <table id="resourceAllocations" class="list">
                    <thead>
                        <tr>
                            <th>
                                <g:message code="org.davisononline.footy.match.fixtureName" default="Fixture"/>
                            </th>
                            <th>
                                <g:message code="org.davisononline.footy.match.kickoff" default="Kick Off (HH:MM)"/>
                            </th>
                            <th>
                                <g:message code="org.davisononline.footy.match.pitch" default="Pitch"/>
                            </th>
                            <th>
                                <g:message code="org.davisononline.footy.match.ref" default="Referee"/>
                            </th>
                            <th>
                                <g:message code="org.davisononline.footy.match.changing" default="Change Rooms"/>
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <g:each in="${fixtures}" var="f" status="i">
                        <g:hiddenField name="fixtures" value="${f.id}"/>
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                            <td id="fixture${f.id}">${f}<br/>(Requested time: <g:formatDate date="${f.dateTime}" format="HH:mm"/>)</td>
                            <td class="date">
                                <g:select onchange="previewAllocations()" name="hour${f.id}" from="${7..21}" value="${g.formatDate(date:f.dateTime, format:'HH')}"/>:<g:select onchange="previewAllocations()" name="minute${f.id}" from="${['00', '15', '30', '45']}" value="${g.formatDate(date:f.dateTime, format:'mm')}"/>
                            </td>
                            <td class="date">
                                <g:select onchange="previewAllocations()" name="pitch${f.id}" from="${availablePitches}" noSelection="['-1':'-- Not Assigned --']" optionKey="id" />
                            </td>
                            <td class="date">
                                <g:select onchange="previewAllocations()" name="ref${f.id}" from="${availableReferees}" noSelection="['-1':'-- Not Assigned --']"  optionKey="id" />
                            </td>
                            <td class="date">
                                <g:select onchange="previewAllocations()" name="chrm${f.id}" from="${availableChangingRooms}" noSelection="['-1':'-- Not Assigned --']" optionKey="id" />
                            </td>
                        </tr>
                        </g:each>
                    </tbody>
                </table>

                <div id="previewAllocations">

                </div>
                <div class="buttons" style="width:100%">
                    <span class="button">
                        <input id="commitAllocationButton" class="save" type="submit" value="${message(code: 'org.davisononline.footy.match.confirmallocations.label', default: 'Confirm Allocations')}" />
                    </span>
                </div>
            </g:form>
            </g:if>
            
            <g:else>
                <g:message code="org.davisononline.footy.match.resources.nofixtures" default="No fixtures for this date"/>
            </g:else>