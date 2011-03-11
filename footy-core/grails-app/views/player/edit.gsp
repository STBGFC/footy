<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'player.label', default: 'Player')}" />
        <title>Edit ${playerInstance}</title>
    </head>
    <body>
        <div class="dialog">
            <p>
                Amend any details of the player you need to, and then click "Update"
            </p>
            
            <div class="nav">
                <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
                <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            </div>
            
            <g:form method="post" >
                <g:hiddenField name="id" value="${playerInstance?.id}" />
                <g:hiddenField name="version" value="${playerInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop">
                                <td  class="name">
                                    <label for="person.givenName"><g:message code="org.davisononline.org.footy.core.contactGivenName.label" default="Given Name(s)" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: playerInstance, field: 'person.givenName', 'errors')}">
                                    <g:textField name="person.givenName" value="${playerInstance?.person.givenName}" />
                                    <g:render template="/fieldError" model="['instance':playerInstance,'field':'person.givenName']"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td  class="name">
                                    <label for="person.familyName"><g:message code="org.davisononline.org.footy.core.contactFamilyName.label" default="Family Name" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: playerInstance, field: 'person.familyName', 'errors')}">
                                    <g:textField name="person.familyName" value="${playerInstance?.person.familyName}" />
                                    <g:render template="/fieldError" model="['instance':playerInstance,'field':'person.familyName']"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td  class="name">
                                    <label for="person.knownAsName"><g:message code="org.davisononline.org.footy.core.contactKnownAsName.label" default="Known As" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: playerInstance, field: 'person.knownAsName', 'errors')}">
                                    <g:textField name="person.knownAsName" value="${playerInstance?.person.knownAsName}" />
                                    <g:render template="/fieldError" model="['instance':playerInstance,'field':'person.knownAsName']"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="dateOfBirth"><g:message code="org.davisononline.org.footy.registration.playerDob.label" default="Date of Birth" /></label>
                                </td>
                                <td valign="top" class="value date">
                                    <g:datePicker name="dateOfBirth" precision="day" years="${(new Date().year-19+1900)..(new Date().year-5+1900)}" value="${playerInstance?.dateOfBirth}"  />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td  class="name">
                                    <label for="parentId"><g:message code="org.davisononline.org.footy.core.playerGuardian.label" default="Parent/Guardian" /></label>
                                </td>
                                <td  class="value">
                                    <g:select name="parentId" from="${parents}" noSelection="[null:'-- Not listed or not applicable --']" optionKey="id" value="${playerInstance?.guardian?.id}"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td  class="name">
                                    <label for="secondParentId"><g:message code="org.davisononline.org.footy.core.playerSecondGuardian.label" default="Second Parent/Guardian" /></label>
                                </td>
                                <td  class="value">
                                    <g:select name="secondParentId" from="${parents}" noSelection="[null:'-- Not listed or not applicable --']" optionKey="id" value="${playerInstance?.secondGuardian?.id}"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="lastRegistrationDate"><g:message code="org.davisononline.org.footy.registration.playerLastRegistration.label" default="Date of Last Registration" /></label>
                                </td>
                                <td valign="top" class="value date">
                                    <g:datePicker name="lastRegistrationDate" precision="day" years="${(new Date().year-3+1900)..(new Date().year+1900)}" value="${playerInstance?.lastRegistrationDate}"  />
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
