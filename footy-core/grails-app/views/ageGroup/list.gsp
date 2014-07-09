
<%@ page import="org.davisononline.footy.core.AgeGroup" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'agegroup.label', default: 'AgeGroup')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <h1>
            <g:message code="org.davisononoline.footy.core.agegrouplistview.text" default="Age Groups at the club"/>
        </h1>
        <div class="list">
            <div class="nav">
                <span class="menuButton"><a class="home" href="${createLink(uri: '/login/profile')}"><g:message code="default.home.label"/></a></span>
                <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </div>
            <table class="list">
                <thead>
                    <tr>
                        <th><g:message code="agegroup.name.label" default="Name" /></th>
                        <th><g:message code="agegroup.coaches.label" default="Coordinator"/></th>
                    </tr>
                </thead>
                <tbody>
                <g:each in="${ageGroupInstanceList}" status="i" var="ageGroupInstance">
                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        <td><g:link action="edit" id="${ageGroupInstance.id}">${ageGroupInstance}</g:link></td>
                        <td>
                            <g:if test="${ageGroupInstance.coordinator}">
                            <g:link controller="person" action="edit" id="${ageGroupInstance.coordinator.id}">
                                ${ageGroupInstance.coordinator}
                            </g:link> (${ageGroupInstance.coordinator.phone1})
                            </g:if>
                        </td>
                    </tr>
                </g:each>
                </tbody>
            </table>
            <div class="paginateButtons">
                <g:paginate total="${ageGroupInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
