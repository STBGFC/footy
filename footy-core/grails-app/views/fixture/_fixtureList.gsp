<%@ page import="org.davisononline.footy.match.Fixture" %>
    <g:set var="now" value="${new Date()}"/>
                <div class="section-title">
                    <div class="left">
                        <g:message code="org.davisononline.footy.match.fixturecal.title" default="Fixture Calendar"/>
                    </div>
                    <div class="clearer">&nbsp;</div>
                </div>

                <div class="section-content">
                    <g:if test="${fixtures.size() > 0}">
                    <table>
                        <tbody>
                        <g:each in="${fixtures}" var="fixture">
                        <%-- set list icon to denote cup/friendly/league --%>
                        <tr>
                            <td>
                                <strong><g:formatDate date="${fixture.dateTime}" format="dd'-'MMM HH:mm"/></strong>
                            </td>
                            <td>
                            <footy:isManager team="${teamInstance}">
                                <g:if test="${fixture.dateTime < now}">
                                <g:link
                                        controller="fixture"
                                        action="addResult"
                                        id="${fixture.id}"
                                        title="Add result/report"
                                        width="450">
                                    ${fixture.opposition}
                                </g:link>
                                </g:if>
                                <g:else>
                                    ${fixture.opposition}
                                </g:else>
                                <g:remoteLink
                                        controller="fixture"
                                        action="delete"
                                        params="[fixtureId:fixture.id,teamId:teamInstance.id]"
                                        update="fixtureList"
                                        on500="alert('An error occurred attempting to delete this fixture')"
                                        on404="alert('Fixture not found')"
                                        title="delete fixture (if resources have been assigned, please also liaise with fixture sec.)"
                                ><r:img dir="images" file="delete.png" plugin="footy-core" alt="del"/></g:remoteLink>
                                <g:if test="${fixture.homeGame && (fixture.resources?.size() > 0 || fixture.referee)}">
                                <g:link controller="resource" action="summary" params="${[year:fixture.dateTime.year+1900, month:fixture.dateTime.month+1, day:fixture.dateTime.date]}">
                                    <r:img dir="images" file="resources.png" plugin="footy-core" alt=""
                                        title="Resources have been assigned to this fixture. Click to view the summary sheet"/>
                                </g:link>
                                </g:if>
                            </footy:isManager>
                            <footy:isNotManager team="${teamInstance}">
                                ${fixture.opposition}
                            </footy:isNotManager>
                            </td>
                            <td>
                                ${fixture.homeGame ? 'H':'A'}&nbsp;${fixture.type == Fixture.CUP_GAME ? '(C)':''}${fixture.type == Fixture.FRIENDLY_GAME ? '(F)':''}
                            </td>
                        </tr>
                        </g:each>
                        <tr>
                            <td colspan="3">
                                <g:message code="org.davisononline.footy.match.fixturetypekey.label" default="(C) = Cup game, (F) = Friendly"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    </g:if>
                    <g:else>
                        <p><g:message code="org.davisononline.footy.match.text.nofixtures" default="No upcoming fixtures listed."/></p>
                    </g:else>

                    <div>
                        <r:img dir="images" file="cal.png" plugin="footy-core" alt="ICS"/>
                        <a href="${createLink(absolute:true, controller:'fixture', action:'calendar', id:teamInstance.id).replace('http','webcal').replace('https','webcal')}"
                            title="Subscribe to ${teamInstance} calendar to view it in Outlook/Thunderbird/iPhone/Android etc. and be kept up to date all the time!">
                            <g:message code="org.davisononline.footy.match.calendarsubscribe.text" default="Subscribe to this fixture calendar"/>
                        </a>
                    </div>

                    <footy:isManager team="${teamInstance}">
                    <div>
                        <r:img dir="images" file="add.png" plugin="footy-core"/>
                        <modalbox:createLink
                                controller="fixture"
                                action="createDialog"
                                id="${teamInstance.id}"
                                title="Create new ${teamInstance} Fixture"
                                width="450">
                            <g:message code="org.davisononline.footy.match.label.create" default="New"/>
                        </modalbox:createLink>
                    </div>
                    </footy:isManager>
                    
                </div>

