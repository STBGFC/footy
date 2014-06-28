<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'person.label', default: 'Login')}" />
        <title>${userCommand.id ? 'Edit' : 'Create'} login for ${personCommand}</title>
    </head>
    <body>
        <h1>Login details for ${personCommand}</h1>
        <div class="dialog">
            <p>
                Select the roles for the new login.  CHOOSE THEM CAREFULLY!  With power comes responsibility.
            </p>

            <div class="nav">
                <g:render template="/shared/editNavButtons" model="${[entityName:entityName]}"/>
            </div>

            <g:form name="loginEditForm" method="post" >
                <g:hiddenField name="id" value="${personCommand?.id}" />
                <g:hiddenField name="version" value="${personCommand?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="username"><g:message code="org.davisononline.org.footy.core.login.name.label" default="Username" /></label>
                                </td>
                                <td valign="top" class="value">
                                    <g:textField name="username" value="${userCommand.username}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label><g:message code="org.davisononline.org.footy.core.login.status.label" default="Status" /></label>
                                </td>
                                <td valign="top" class="value">
                                    <div><g:checkBox name="accountExpired" value="${userCommand.accountExpired}"/> Account Expired</div>
                                    <div><g:checkBox name="enabled" value="${userCommand.enabled}"/> Account Enabled</div>
                                    <div><g:checkBox name="accountLocked" value="${userCommand.accountLocked}"/> Account Locked</div>
                                    <div><g:checkBox name="passwordExpired" value="${userCommand.passwordExpired}"/> Password Expired</div>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="roles"><g:message code="org.davisononline.org.footy.core.login.roles.label" default="Roles" /></label>
                                </td>
                                <td valign="top" class="value">
                                    <g:each var="entry" in="${roleMap}">
                                    <div>
                                        <g:checkBox name="${entry.key.authority}" value="${entry.value}"/>
                                        ${entry.key.authority.replace('ROLE', '').replace('_', ' ').encodeAsHTML()}
                                    </div>
                                    </g:each>
                                </td>
                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit id="_action_saveUser" class="save" action="saveLogin" value="${message(code: 'default.button.save.label', default: 'Save')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
