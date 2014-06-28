<%@ page import="org.davisononline.footy.core.SecUser" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <g:set var="entityName" value="${message(code: 'person.label', default: 'Login')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<h1>
    People with login capabilities to the site
</h1>
<div class="list">
    <div class="nav">
        <span class="menuButton"><a class="home" href="${createLink(uri: '/login/profile')}"><g:message code="default.home.label"/></a></span>
        <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="['People']" /></g:link></span>
    </div>
    <table class="list">
        <thead>
        <tr>
            <th><g:message code='person.name.label' default='Name'/></th>
            <th><g:message code='person.user.label' default='Username' /></th>
            <th><g:message code="person.canlogin.label" default="Login Status" /></th>
            <th><g:message code="person.userauthorities.label" default="Roles"/></th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <sec:ifAnyGranted roles="ROLE_SYSADMIN">
            <g:set var="editAction" value="editLogin"/>
        </sec:ifAnyGranted>
        <sec:ifNotGranted roles="ROLE_SYSADMIN">
            <g:set var="editAction" value="edit"/>
        </sec:ifNotGranted>
        <g:each in="${personInstanceList}" status="i" var="personInstance">
            <g:set var="user" value="${personInstance.user}"/>
            <g:set var="blocked" value="${user.accountExpired || user.accountLocked || !user.enabled}"/>
            <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                <td><g:link action="${editAction}" id="${personInstance.id}">${personInstance}</g:link></td>
                <td>${fieldValue(bean: user, field: "username")}</td>
                <td>
                    <img align="middle" src="${r.resource(dir:'images',file:'registration-' + (blocked ? 'expired' : 'paid') + '.png', plugin:'footy-core')}"/>
                    <g:if test="${blocked}">
                        ${user.accountLocked ? "Locked Account" : ""}
                        ${user.accountExpired ? "Account pending deletion" : ""}
                        ${user.enabled ? "" : "Disabled Account"}
                    </g:if>
                </td>
                <td>
                    ${user.authorities*.authority.join('<br/>').replace('ROLE', '').replace('_', ' ')}
                </td>
                <td><g:link action="toggleLock" id="${personInstance.id}">${user.accountLocked ? 'un' : ''}lock this account</g:link></td>
            </tr>
        </g:each>
        </tbody>
    </table>
    <div class="paginateButtons">
        <g:paginate total="${personInstanceTotal}" />
    </div>
</div>
</body>
</html>
