
<%@ page import="org.davisononline.footy.core.QualificationType" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'qualificationType.label', default: 'QualificationType')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <h1>
            <g:message code="org.davisononoline.footy.core.qualificationtypelistview.text" default="Qualifications that can be assigned to people at the club"/>
        </h1>
        <div class="list">
            <div class="nav">
                <span class="menuButton"><a class="home" href="${createLink(uri: '/login/profile')}"><g:message code="default.home.label"/></a></span>
                <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </div>
            <table class="list">
                <thead>
                    <tr>
                        <g:sortableColumn property="name" title="${message(code: 'qualificationType.name.label', default: 'Name')}" />
                        <g:sortableColumn property="category" title="${message(code: 'qualificationType.category.label', default: 'Category')}" />
                        <th><g:message code="qualificationType.yearsValidFor.label" default="Years Valid For" /></th>
                    </tr>
                </thead>
                <tbody>
                <g:each in="${qualificationTypeInstanceList}" status="i" var="qualificationTypeInstance">
                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        <td><g:link action="edit" id="${qualificationTypeInstance.id}">${fieldValue(bean: qualificationTypeInstance, field: "name")}</g:link></td>
                        <td>${fieldValue(bean: qualificationTypeInstance, field: "category")}</td>
                        <td>${fieldValue(bean: qualificationTypeInstance, field: "yearsValidFor")}</td>
                    </tr>
                </g:each>
                </tbody>
            </table>
            <div class="paginateButtons">
                <g:paginate total="${qualificationTypeInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
