<%@ page import="org.davisononline.footy.match.Fixture" %>
<r:require module="modalBox"/>

                <div class="section-title">
                    <div class="left">
                        <g:message code="org.davisononline.footy.match.fixturecal.title" default="Fixture Calendar"/>
                    </div>
                    <div class="right">
                    <g:if test="${totalFixtureCount > 0}">
                        <g:link controller="fixture" action="list" id="${myteam.id}" title="View All">
                            <r:img dir="images/skin" file="database_table.png" plugin="footy-core" alt="View All"/>
                        </g:link>
                    </g:if>
                        <a href="${createLink(absolute:true, controller:'fixture', action:'calendar', id:myteam.id).replace('http','webcal').replace('https','webcal')}"
                            title="Subscribe to ${myteam} calendar (Outlook/Thunderbird/iPhone etc.)">
                            <r:img dir="images" file="cal.png" plugin="footy-core" alt="ICS"/>
                        </a>
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
                            <footy:isManager team="${myteam}">
                                <g:link
                                        controller="fixture"
                                        action="addResult"
                                        id="${fixture.id}"
                                        title="Add result/report"
                                        width="450">
                                    ${fixture.opposition}
                                </g:link>
                                <g:remoteLink
                                        controller="fixture"
                                        action="delete"
                                        params="[fixtureId:fixture.id,teamId:myteam.id]"
                                        update="fixtureList"
                                        on500="alert('An error occurred attempting to delete this fixture')"
                                        on404="alert('Fixture not found')"
                                        title="delete fixture (if resources have been assigned, please also liaise with fixture sec.)"
                                ><r:img dir="images/skin" file="database_delete.png" plugin="footy-core" alt="del"/></g:remoteLink>
                                <g:if test="${fixture.homeGame && (fixture.resources?.size() > 0 || fixture.referee)}">
                                <g:link controller="resource" action="summary" params="${[year:fixture.dateTime.year+1900, month:fixture.dateTime.month+1, day:fixture.dateTime.date]}">
                                    <img
                                        src="${createLinkTo(dir:'images', file:'whistle_icon.png', plugin:'footy-match')}"
                                        alt=""
                                        title="Resources have been assigned to this fixture. Click to view the summary sheet" />
                                </g:link>
                                </g:if>
                            </footy:isManager>
                            <footy:isNotManager team="${myteam}">
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

                    <footy:isManager team="${myteam}">
                    <r:img dir="images/skin" file="database_add.png" plugin="footy-core"/>
                    <modalbox:createLink
                            controller="fixture"
                            action="createDialog"
                            id="${myteam.id}"
                            title="Create new ${myteam} Fixture"
                            width="450">
                        <g:message code="org.davisononline.footy.match.label.create" default="New"/>
                    </modalbox:createLink>
                    </footy:isManager>
                    
                </div>