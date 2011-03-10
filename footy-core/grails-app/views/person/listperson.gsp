
<%@ page import="org.davisononline.footy.core.Person" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'person.label', default: 'Person')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="list">
            <p>
                People associated with the club
            </p>
            <div class="nav">
                <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
                <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </div>
            
                <table class="list">
                    <thead>
                        <tr>
                            <g:sortableColumn property="familyName" title="${message(code: 'person.familyName.label', default: 'Family Name')}" />
                            <g:sortableColumn property="givenName" title="${message(code: 'person.givenName.label', default: 'Given Name(s)')}" />
                            <g:sortableColumn property="knownAsName" title="${message(code: 'person.knownAsName.label', default: 'Known As')}" />
                            <g:sortableColumn property="email" title="${message(code: 'person.email.label', default: 'Email')}" />
                            <th><g:message code="person.address.label" default="Address" /></th>
                            <th><g:message code="actions.label" default="&nbsp;" /></th>
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${personInstanceList}" status="i" var="personInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                            <td>${fieldValue(bean: personInstance, field: "familyName")}</td>
                            <td>${fieldValue(bean: personInstance, field: "givenName")}</td>
                            <td>${fieldValue(bean: personInstance, field: "knownAsName")}</td>
                            <td>${fieldValue(bean: personInstance, field: "email")}</td>
                            <td>${fieldValue(bean: personInstance, field: "address")}</td>
                            <td><g:link action="edit" id="${personInstance.id}">edit</g:link></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${personInstanceTotal}" />
            </div>
    </body>
</html>
