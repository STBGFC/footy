<%@ page import="org.davisononline.footy.core.RegistrationTier" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'registrationTier.label', default: 'RegistrationTier')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <h1>
            Create or edit details of the registration tier below
        </h1>
        <div class="dialog">
            <div class="nav">
                <g:render template="/shared/editNavButtons" model="${[entityName:entityName]}"/>
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
                                    <g:render template="/shared/fieldError" model="['instance':registrationTierInstance,'field':'name']" plugin="footy-core"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="validUntil"><g:message code="org.davisononline.org.footy.core.registrationTier.validUntil.label" default="Date that registrations will expire" /></label>
                                </td>
                                <td valign="top" class="date value ${hasErrors(bean: registrationTierInstance, field: 'validUntil', 'errors')}">
                                    <g:set var="now" value="${new Date()}"/>
                                    <g:datePicker name="validUntil" precision="day" years="${(now.year+1900)..(now.year+1902)}" value="${registrationTierInstance?.validUntil}" />
                                    <g:render template="/shared/fieldError" model="['instance':registrationTierInstance,'field':'validUntil']" plugin="footy-core"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="amount"><g:message code="org.davisononline.org.footy.core.registrationTier.amount.label" default="Amount" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: registrationTierInstance, field: 'amount', 'errors')}">
                                    <g:textField name="amount" value="${fieldValue(bean: registrationTierInstance, field: 'amount')}" />
                                    <g:render template="/shared/fieldError" model="['instance':registrationTierInstance,'field':'amount']" plugin="footy-core"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="repeatDiscount"><g:message code="org.davisononline.org.footy.core.registrationTier.repeatDiscount.label" default="Repeat Discount" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: registrationTierInstance, field: 'repeatDiscount', 'errors')}">
                                    <g:textField name="repeatDiscount" value="${fieldValue(bean: registrationTierInstance, field: 'repeatDiscount')}" />
                                    <g:render template="/shared/fieldError" model="['instance':registrationTierInstance,'field':'repeatDiscount']" plugin="footy-core"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="siblingDiscount"><g:message code="org.davisononline.org.footy.core.registrationTier.siblingDiscount.label" default="Sibling Discount" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: registrationTierInstance, field: 'siblingDiscount', 'errors')}">
                                    <g:textField name="siblingDiscount" value="${fieldValue(bean: registrationTierInstance, field: 'siblingDiscount')}" />
                                    <g:render template="/shared/fieldError" model="['instance':registrationTierInstance,'field':'siblingDiscount']" plugin="footy-core"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="enabled"><g:message code="org.davisononline.org.footy.core.registrationTier.enabled.label" default="Enabled" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: registrationTierInstance, field: 'enabled', 'errors')}">
                                    <g:checkBox name="enabled" value="${registrationTierInstance?.enabled}" />
                                    <g:render template="/shared/fieldError" model="['instance':registrationTierInstance,'field':'enabled']" plugin="footy-core"/>
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
