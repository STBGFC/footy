
<%@ page import="org.davisononline.footy.core.AgeGroup" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <g:set var="entityName" value="${message(code: 'ageGroup.label', default: 'AgeGroup')}" />
    <title>
        <g:if test="${ageGroupInstance?.id}">${ageGroupInstance}</g:if>
        <g:else><g:message code="default.create.label" args="[entityName]" /></g:else>
    </title>
</head>
<body>
<h1>
    ${ageGroupInstance.id ? 'Edit ' + ageGroupInstance : 'Create'} age group details below
</h1>
<div class="dialog">
    <div class="nav">
        <g:render template="/shared/editNavButtons" model="${[entityName:entityName]}"/>
    </div>

    <g:form method="post"  enctype="multipart/form-data">
        <g:hiddenField name="id" value="${ageGroupInstance?.id}" />
        <g:hiddenField name="version" value="${ageGroupInstance?.version}" />
        <div class="dialog">
            <table>
                <tbody>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="year"><g:message code="ageGroup.year.label" default="Age Band"/></label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: ageGroupInstance, field: 'year', 'errors')}">
                        <g:render template="/shared/fieldError" model="['instance':ageGroupInstance,'field':'year']" plugin="footy-core"/>
                        <div>
                            <g:radioGroup name="under" values="[true, false]" labels="['Under', 'Over']" value="${ageGroupInstance?.under}">
                            ${it.label} ${it.radio}
                            </g:radioGroup>
                        </div>
                        <g:select name="year" from="${AgeGroup.constraints.year.inList}"
                                  value="${fieldValue(bean: ageGroupInstance, field: 'year')}"/>
                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="coordinator.id"><g:message code="org.davisononline.footy.core.ageGroup.coordinator.label" default="Coordinator" /></label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: ageGroupInstance, field: 'coordinator', 'errors')}">
                        <g:select name="coordinator.id" from="${coordinators}" noSelection="[null:'-- Not listed or not applicable --']" optionKey="id" value="${ageGroupInstance?.coordinator?.id}"  />
                    </td>
                </tr>

                </tbody>
            </table>
        </div>
        <div class="buttons">
            <g:if test="${ageGroupInstance?.id}">
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