<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'player.label', default: 'Player')}" />
        <title>Edit ${playerInstance.person}</title>
    </head>
    <body>
        <div class="main" id="main-two-columns">
			<div class="left" id="main-left">
                <h1>Amend any details of the player you need to, and then click "Update"</h1>
                <div class="dialog">

                    <div class="nav">
                        <g:render template="/shared/editNavButtons" model="${[entityName:entityName]}"/>
                        <span class="menuButton">
                        <g:if test="${playerInstance.team}">
                            <g:link class="list" controller="team" action="show" params="${[ageBand:playerInstance.team.ageBand, teamName: playerInstance.team.name]}">
                                ${playerInstance.team}
                            </g:link>
                        </g:if>
                        </span>
                    </div>

                    <g:form name="playerEditForm" method="post" >
                        <g:hiddenField name="id" value="${playerInstance?.id}" />
                        <g:hiddenField name="version" value="${playerInstance?.version}" />
                        <div class="dialog">
                            <table>
                                <tbody>

                                    <g:render template="/player/playerFormBody" plugin="footy-core"/>

                                    <tr class="prop">
                                        <td  class="name">
                                            <label for="secondGuardian.id"><g:message code="org.davisononline.org.footy.core.playerSecondGuardian.label" default="Second Parent/Guardian" /></label>
                                        </td>
                                        <td  class="value">
                                            <g:select name="secondGuardian.id" from="${parents}" noSelection="[null:'-- Not listed or not applicable --']" optionKey="id" value="${playerInstance?.secondGuardian?.id}"/>
                                        </td>

                                        <g:render template="/player/teamFields" plugin="footy-core" />
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="buttons">
                            <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                            <sec:ifAnyGranted roles="ROLE_OFFICER">
                            <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                            </sec:ifAnyGranted>
                        </div>
                    </g:form>
                </div>
            </div>



            <div class="right sidebar" id="sidebar">

                <g:if test="${playerInstance.guardian || playerInstance.secondGuardian}">
				<div class="section">
					<div class="section-title">
						<div class="left">
                            <g:message code="org.davisononline.footy.core.parents.label"
                               default="Player Links" />
                        </div>
                        <div class="clearer">&nbsp;</div>
					</div>

					<div class="section-content">
                        <ul class="nice-list">
                            <g:each in="${[playerInstance.guardian,playerInstance.secondGuardian]}" var="p">
                            <g:if test="${p}">
                            <li>Parent: <g:link controller="person" action="edit" id="${p.id}">${p}</g:link></li>
                            </g:if>
                            </g:each>
                            <li>
                                Team:
                                <g:set var="t" value="${playerInstance.team}"/>
                                <g:if test="${t}">
                                    <g:link controller="team" action="show" params="${[ageBand:t.ageBand, teamName:t.name]}">
                                        ${t}
                                    </g:link>
                                </g:if>
                                <g:else>Unassigned</g:else>
                            </li>
                        </ul>
					</div>
				</div>
                </g:if>

                <sec:ifAnyGranted roles="ROLE_CLUB_ADMIN">
                <div class="section">
					<div class="section-title">
						<div class="left">
                            <g:message code="org.davisononline.footy.core.previousregistrations.label"
                                default="Current Registration" />
                        </div>
                        <div class="clearer">&nbsp;</div>
					</div>

					<div class="section-content">
                        <div>
                            <footy:registrationStatus player="${playerInstance}"/>
                            <g:formatDate date="${playerInstance.currentRegistration.date}" format="dd MMMM yyyy"/>
                        </div>
					</div>
				</div>
                </sec:ifAnyGranted>
            </div>

            <div class="clearer"></div>

        </div>
    </body>
</html>
