<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'person.label', default: 'Person')}" />
        <title>${personCommand?.id ? "Edit details for: " + personCommand : "Add New Person"}</title>
    </head>
    <body>
        <div class="main" id="main-two-columns">
			<div class="left" id="main-left">
                <h1>${personCommand?.id ? "Edit details for: " + personCommand : "Add New Person"}</h1>
                <div class="dialog">
                    <g:if test="${!personCommand?.id}">
                    <p>
                        Please add details of the new person here.  Do NOT use this form to register a new player,
                        that must be done using the <g:link controller="registration">registration wizard</g:link>
                        instead.
                    </p>
                    </g:if>
                    <g:else>
                    <p>
                        Amend any details of the person you need to, and then click "Update"
                    </p>
                    </g:else>

                    <div class="nav">
                        <g:render template="/shared/editNavButtons" model="${[entityName:entityName]}"/>
                    </div>

                    <g:form name="personEditForm" method="post" >
                        <g:hiddenField name="id" value="${personCommand?.id}" />
                        <g:hiddenField name="version" value="${personCommand?.version}" />
                        <div class="dialog">
                            <table>
                                <tbody>
                                    <g:render template="personFormBody" plugin="footy-core" />
                                    <%-- following is kept off the template so it doesn't appear when registering for parents --%>
                                    <tr class="prop"><td class="name" colspan="2">Coaches/managers/referees only</td></tr>
                                    <g:set var="now" value="${new Date()}"/>
                                    <tr class="prop">
                                        <td  class="name">
                                            <label for="dateOfBirth"><g:message code="org.davisononline.org.footy.core.dateOfBirth.label" default="Date Of Birth" /></label>
                                        </td>
                                        <td  class="value date ${hasErrors(bean: personCommand, field: 'dateOfBirth', 'errors')}">
                                            <g:datePicker name="dateOfBirth" default="none" noSelection="['':'']" precision="day" years="${(now.year-79+1900)..(now.year-15+1900)}" value="${personCommand?.dateOfBirth}" />
                                            <g:render template="/shared/fieldError" model="['instance':personCommand,'field':'dateOfBirth']" plugin="footy-core"/>
                                        </td>
                                    </tr>

                                    <tr class="prop">
                                        <td  class="name">
                                            <label for="fanNumber"><g:message code="org.davisononline.org.footy.core.fanNumber.label" default="FAN Number" /></label>
                                        </td>
                                        <td  class="value ${hasErrors(bean: personCommand, field: 'fanNumber', 'errors')}">
                                            <g:textField name="fanNumber" value="${personCommand?.fanNumber}" />
                                            <g:render template="/shared/fieldError" model="['instance':personCommand,'field':'fanNumber']" plugin="footy-core"/>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="buttons">
                            <g:if test="${personCommand?.id}">
                            <span class="button"><g:actionSubmit id="_action_save" class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                            <sec:ifAnyGranted roles="ROLE_OFFICER">
                            <span class="button"><g:actionSubmit id="_action_delete" class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                            </sec:ifAnyGranted>
                            </g:if>
                            <g:else>
                            <span class="button"><g:actionSubmit id="_action_save" class="save" action="save" value="${message(code: 'default.button.save.label', default: 'Save')}" /></span>
                            </g:else>
                        </div>
                    </g:form>
                </div>
            </div>

            <div class="right sidebar" id="sidebar">

                <g:if test="${personCommand.id}">
				<div class="section">
					<div class="section-title">
						<div class="left">
                            <g:message code="org.davisononline.footy.core.qualifications.label"
                               default="Qualifications Attained" />
                        </div>
                        <div class="clearer">&nbsp;</div>
					</div>

					<div class="section-content">
                        <div id="qualifications">
                            <g:render template="qualificationsList" model="[person:personCommand]" plugin="footy-core"/>
                        </div>
                        <sec:ifAnyGranted roles="ROLE_CLUB_ADMIN">
                        <modalbox:createLink
                                controller="person"
                                action="assignQualification"
                                id="${personCommand?.id}"
                                title="Add qualifications for ${personCommand?.givenName}"
                                width="450">
                            <g:message code="org.davisononline.footy.core.addqualifications.link" default="Add New"/>
                            <img src="${createLinkTo(dir:'images', file:'add.png')}" alt=""/>
                        </modalbox:createLink>
                        </sec:ifAnyGranted>
					</div>
				</div>

                <div class="section">
					<div class="section-title">
						<div class="left">
                            <g:message code="org.davisononline.footy.core.children.label"
                            default="Parent or Guardian of" />
                        </div>
                        <div class="clearer">&nbsp;</div>
					</div>

					<div class="section-content">
						<ul class="nice-list">
							<g:each in="${[Player.findAllByGuardian(personCommand),Player.findAllBySecondGuardian(personCommand)].flatten()}" var="child">
                            <li>
                                <g:link controller="player" action="edit" id="${child.id}">${child}</g:link>
                            </li>
                            </g:each>
						</ul>
					</div>
				</div>

                <sec:ifAnyGranted roles="ROLE_OFFICER">
                <g:if test="${personCommand.payments}">
                <div class="section">
					<div class="section-title">
						<div class="left">
                            <g:message code="org.davisononline.footy.core.payments.label"
                                default="Invoices and Payments" />
                        </div>
                        <div class="clearer">&nbsp;</div>
					</div>

					<div class="section-content">
						<g:render template="paymentsList" model="[payments:personCommand.payments]" plugin="footy-core"/>
					</div>
				</div>
                </g:if>
                <div class="section">
					<div class="section-title">
						<div class="left">
                            <g:message code="org.davisononline.footy.core.userlogin.label"
                                default="Login Details" />
                        </div>
                        <div class="clearer">&nbsp;</div>
					</div>

					<div class="section-content">
						<ul class="nice-list">
                            <g:if test="${personCommand.user}">
                            <g:set var="user" value="${personCommand.user}"/>
                            <g:set var="blocked" value="${user.accountExpired || user.accountLocked || !user.enabled}"/>
							<li>
                                Username: <strong>${user.username}</strong>
                                <img align="middle" alt="${blocked ? 'Un' : ''}able to login" src="${r.resource(dir:'images',file:'registration-' + (blocked ? 'expired' : 'paid') + '.png', plugin:'footy-core')}"/>
                                <g:if test="${blocked}">
                                    (${user.accountLocked ? "Locked Account" : ""}
                                    ${user.accountExpired ? "Account pending deletion" : ""}
                                    ${user.enabled ? "" : "Disabled Account"})
                                </g:if>
                            </li>
                            <li>
                                <g:link action="toggleLock" id="${personCommand.id}">${user.accountLocked ? 'un' : ''}lock this account</g:link>
                            </li>
                            <sec:ifAnyGranted roles="ROLE_SYSADMIN">
                            <li>
                                <g:link action="editLogin" id="${personCommand.id}">edit user or roles</g:link>
                            </li>
                            </sec:ifAnyGranted>
						</g:if>
                        <g:else>
                            <sec:ifAnyGranted roles="ROLE_SYSADMIN">
                            <li>
                                <g:link action="editLogin" id="${personCommand.id}">Create a login for ${personCommand}</g:link>
                            </li>
                            </sec:ifAnyGranted>
                            <sec:ifNotGranted roles="ROLE_SYSADMIN">
                            <li>
                                No login for ${personCommand}
                            </li>
                            </sec:ifNotGranted>
                        </g:else>
                        </ul>
					</div>
				</div>
                </sec:ifAnyGranted>
            </g:if>
            </div>

            <div class="clearer"></div>

        </div>
    </body>
</html>
