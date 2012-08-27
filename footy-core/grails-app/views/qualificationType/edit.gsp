
<%@ page import="org.davisononline.footy.core.QualificationType" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'qualificationType.label', default: 'QualificationType')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <h1>
            Create or edit details of the qualification type below
        </h1>
        <div class="dialog">
            <div class="nav">
                <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
                <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            </div>

            <g:form method="post" >
                <g:hiddenField name="id" value="${qualificationTypeInstance?.id}" />
                <g:hiddenField name="version" value="${qualificationTypeInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="category"><g:message code="qualificationType.category.label" default="Category" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: qualificationTypeInstance, field: 'category', 'errors')}">
                                    <g:select name="category" from="${qualificationTypeInstance.constraints.category.inList}" value="${qualificationTypeInstance?.category}" valueMessagePrefix="qualificationType.category"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="qualificationType.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: qualificationTypeInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${qualificationTypeInstance?.name}" />
                                    <g:render template="/shared/fieldError" model="['instance':qualificationTypeInstance,'field':'name']" plugin="footy-core"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="yearsValidFor"><g:message code="qualificationType.yearsValidFor.label" default="Years Valid For" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: qualificationTypeInstance, field: 'yearsValidFor', 'errors')}">
                                    <g:select name="yearsValidFor" from="${[0,1,2,3,5]}" value="${fieldValue(bean: qualificationTypeInstance, field: 'yearsValidFor')}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <g:if test="${qualificationTypeInstance?.id}">
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
