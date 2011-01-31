
<%@ page import="org.davisononline.footy.tournament.Entry" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'entry.label', default: 'Entry')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                            <g:sortableColumn property="ageGroup" title="${message(code: 'entry.ageGroup.label', default: 'Age Group')}" />
                            <g:sortableColumn property="club" title="${message(code: 'entry.teamName.label', default: 'Team Name')}" />
                            <th>${message(code: 'entry.contactName.label', default: 'Contact')}</th>
                            <g:sortableColumn property="payment" title="${message(code: 'entry.payment.label', default: 'Payment Status')}" />
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${entryInstanceList}" status="i" var="entryInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                            <td>${fieldValue(bean: entryInstance, field: "ageGroup")}</td>
                            <td>
                                <g:link action="show" id="${entryInstance.id}">
                                    ${fieldValue(bean: entryInstance, field: "club")}
                                    ${fieldValue(bean: entryInstance, field: "teamName")}
                                </g:link>
                            </td>
                            <td>
                                ${fieldValue(bean: entryInstance, field: "contactName")}
                                (${entryInstance.mobileNumber})
                            </td>
                            <td>
                                ${entryInstance.payment?.status}
                            </td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${entryInstanceTotal}" max="30"/>
            </div>
        </div>
    </body>
</html>
