<%@ page import="org.davisononline.footy.core.RegistrationTier" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'registrationTier.label', default: 'RegistrationTier')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="dialog">
            <p>
                Create or edit details of the registration tier below
            </p>
            <div class="nav">
                <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
                <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            </div>

            <g:form method="post" >
                <g:hiddenField name="id" value="${registrationTierInstance?.id}" />
                <g:hiddenField name="version" value="${registrationTierInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="org.davisononline.org.footy.core.registrationTier.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: registrationTierInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${registrationTierInstance?.name}" />
                                    <g:render template="/fieldError" model="['instance':registrationTierInstance,'field':'name']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="amount"><g:message code="org.davisononline.org.footy.core.registrationTier.amount.label" default="Amount" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: registrationTierInstance, field: 'amount', 'errors')}">
                                    <g:textField name="amount" value="${fieldValue(bean: registrationTierInstance, field: 'amount')}" />
                                    <g:render template="/fieldError" model="['instance':registrationTierInstance,'field':'amount']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="repeatDiscount"><g:message code="org.davisononline.org.footy.core.registrationTier.repeatDiscount.label" default="Repeat Discount" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: registrationTierInstance, field: 'repeatDiscount', 'errors')}">
                                    <g:textField name="repeatDiscount" value="${fieldValue(bean: registrationTierInstance, field: 'repeatDiscount')}" />
                                    <g:render template="/fieldError" model="['instance':registrationTierInstance,'field':'repeatDiscount']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="siblingDiscount"><g:message code="org.davisononline.org.footy.core.registrationTier.siblingDiscount.label" default="Sibling Discount" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: registrationTierInstance, field: 'siblingDiscount', 'errors')}">
                                    <g:textField name="siblingDiscount" value="${fieldValue(bean: registrationTierInstance, field: 'siblingDiscount')}" />
                                    <g:render template="/fieldError" model="['instance':registrationTierInstance,'field':'siblingDiscount']"/>
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <g:if test="${registrationTierInstance?.id}">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                    </g:if>
                    <g:else>
                    <span class="button"><g:actionSubmit class="save" action="save" value="${message(code: 'default.button.save.label', default: 'Save')}" /></span>
                    </g:else>
                </div>
            </g:form>
        </div>
    </body>
</html>
