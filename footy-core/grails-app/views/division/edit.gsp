
<%@ page import="org.davisononline.footy.core.Division" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'division.label', default: 'Division')}" />
        <title>
            <g:if test="${divisionInstance?.id}">${divisionInstance.name}</g:if>
            <g:else><g:message code="default.create.label" args="[entityName]" /></g:else>
        </title>
    </head>
    <body>
        <h1>
            Create or edit the division details below
        </h1>
        <div class="dialog">
            <div class="nav">
                <g:render template="/shared/editNavButtons" model="${[entityName:entityName]}"/>
            </div>

            <g:form method="post"  enctype="multipart/form-data">
                <g:hiddenField name="id" value="${divisionInstance?.id}" />
                <g:hiddenField name="version" value="${divisionInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <g:message code="division.league.label" default="League"/>
                                </td>
                                <td valign="top" class="value">
                                    ${divisionInstance.league}
                                    <g:hiddenField name="league.id" value="${divisionInstance?.league.id}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="ageBand"><g:message code="division.ageBand.label" default="Age Band"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: divisionInstance, field: 'ageBand', 'errors')}">
                                    <g:select name="ageBand" from="${8..18}"
                                              value="${fieldValue(bean: divisionInstance, field: 'ageBand')}"/>
                                    <g:render template="/shared/fieldError" model="['instance':divisionInstance,'field':'ageBand']" plugin="footy-core"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="division.name.label" default="Name"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: divisionInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" maxlength="30" value="${divisionInstance?.name}"/>
                                    <g:render template="/shared/fieldError" model="['instance':divisionInstance,'field':'name']" plugin="footy-core"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="index"><g:message code="division.index.label" default="Index"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: divisionInstance, field: 'index', 'errors')}">
                                    <g:textField name="index" value="${fieldValue(bean: divisionInstance, field: 'index')}"/>
                                    <g:render template="/shared/fieldError" model="['instance':divisionInstance,'field':'index']" plugin="footy-core"/>
                                </td>
                            </tr>


                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="code"><g:message code="division.code.label" default="Code"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: divisionInstance, field: 'code', 'errors')}">
                                    <g:textField name="code" value="${divisionInstance?.code}"/>
                                    <g:render template="/shared/fieldError" model="['instance':divisionInstance,'field':'code']" plugin="footy-core"/>
                                </td>
                            </tr>


                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <g:if test="${divisionInstance?.id}">
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