
<%@ page import="org.davisononline.footy.core.Person" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'person.label', default: 'Person')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
        <export:resource/>
    </head>
    <body>
        <h1>
            People associated with the club
        </h1>
        <div class="list">
            <div class="nav">
                <span class="menuButton"><a class="home" href="${createLink(uri: '/login/profile')}"><g:message code="default.home.label"/></a></span>
                <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
                <span class="menuButton"><g:link class="list" action="list" controller="player"><g:message code="default.list.label" args="['Player']" /></g:link></span>
                <span class="menuButton"><g:link class="list" action="listLogins"><g:message code="default.list.label" args="['Login']" /></g:link></span>
            </div>
            
                <table class="list">
                    <thead>
                        <tr>
                            <g:sortableColumn property="familyName" title="${message(code: 'person.name.label', default: 'Name')}" />
                            <g:sortableColumn property="email" title="${message(code: 'person.email.label', default: 'Email')}" />
                            <g:sortableColumn property="fanNumber" title="${message(code: 'person.fannumber.label', default: 'FAN number (coaches)')}" />
                            <th><g:message code='person.phone1.label' default='Tel.' />
                            <th><g:message code="person.address.label" default="Address" /></th>
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${personInstanceList}" status="i" var="personInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                            <td><g:link action="edit" id="${personInstance.id}">${personInstance}</g:link></td>
                            <td>${fieldValue(bean: personInstance, field: "email")}</td>
                            <td>${fieldValue(bean: personInstance, field: "fanNumber")}</td>
                            <td>${personInstance.bestPhone().encodeAsHTML()}</td>
                            <td>${fieldValue(bean: personInstance, field: "address")}</td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
                <div class="paginateButtons">
                    <g:paginate total="${personInstanceTotal}" />
                </div>
                <export:formats formats="['excel', 'pdf']" />
            </div>
    </body>
</html>
