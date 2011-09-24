<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'person.label', default: 'Person')}" />
        <title>${personCommand?.id ? "Edit details for: " + personCommand : "Add New Person"}</title>
    </head>
    <body>
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

        <g:if test="${personCommand.id}">
        <div id="newspanel">
            <div class="newsbox">
                <h2>
                    <g:message code="org.davisononline.footy.core.qualifications.label"
                            default="Qualifications Attained" />
                </h2>
                <div id="qualifications" class="value">
                    <g:render template="qualificationsList" model="[person:personCommand]" plugin="footy-core"/>
                </div>
                <sec:ifAnyGranted roles="ROLE_CLUB_ADMIN">
                <modalbox:createLink
                        controller="person"
                        action="assignQualification"
                        id="${personCommand?.id}"
                        title="Add qualifications for ${personCommand?.givenName}"
                        width="450">
                    <img src="${createLinkTo(dir:'images/skin', file:'database_add.png')}" alt=""/>
                    <g:message code="org.davisononline.footy.core.addqualifications.link" default="Add New"/>
                </modalbox:createLink>
                </sec:ifAnyGranted>
            </div>

            <div class="newsbox">
                <h2>
                    <g:message code="org.davisononline.footy.core.children.label"
                            default="Parent or Guardian of" />
                </h2>
                <ul>
                <g:each in="${[Player.findAllByGuardian(personCommand),Player.findAllBySecondGuardian(personCommand)].flatten()}" var="child">
                    <li>
                        <g:link controller="player" action="edit" id="${child.id}">${child}</g:link>
                    </li>
                </g:each>
                </ul>
            </div>

            <sec:ifAnyGranted roles="ROLE_OFFICER">
            <g:if test="${personCommand.payments}">
            <div class="newsbox">
                <h2>
                    <g:message code="org.davisononline.footy.core.payments.label" default="Invoices and Payments" />
                </h2>
                <div id="payments">
                    <g:render template="paymentsList" model="[payments:personCommand.payments]" plugin="footy-core"/>
                </div>
            </div>
            </g:if>
            </sec:ifAnyGranted>
            </g:if>
        </div>
    </body>
</html>
