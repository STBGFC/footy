
<%@ page import="org.davisononline.footy.tournament.Entry" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'entry.label', default: 'Entry')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="body">
        <div class="nav">
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="apply"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="entry.club.label" default="Club" /></td>
                            <td valign="top" class="value"><g:link controller="club" action="show" id="${entryInstance?.club?.id}">${entryInstance?.club?.encodeAsHTML()}</g:link></td>
                        </tr>
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="entry.teamName.label" default="Team" /></td>
                            <td valign="top" class="value">
                                <strong>${fieldValue(bean: entryInstance, field: "ageGroup")} ${fieldValue(bean: entryInstance, field: "teamName")}</strong>
                            </td>
                        </tr>
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="entry.league.label" default="League" /></td>
                            <td valign="top" class="value">
                                <g:if test="${entryInstance.league.startsWith('Other')}">${entryInstance.otherLeague}</g:if>
                                <g:else>${entryInstance.league}</g:else>
                                Div. ${entryInstance.division}
                            </td>
                        </tr>
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="entry.contactName.label" default="Contact Name" /></td>
                            <td valign="top" class="value">${fieldValue(bean: entryInstance, field: "contactName")}</td>
                        </tr>
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="entry.phoneNumber.label" default="Phone Number" /></td>
                            <td valign="top" class="value">${fieldValue(bean: entryInstance, field: "phoneNumber")}</td>
                        </tr>
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="entry.mobileNumber.label" default="Mobile Number" /></td>
                            <td valign="top" class="value">${fieldValue(bean: entryInstance, field: "mobileNumber")}</td>
                        </tr>
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="entry.email.label" default="Email" /></td>
                            <td valign="top" class="value">
                                <a href="mailto:${fieldValue(bean: entryInstance, field: 'email')}">${fieldValue(bean: entryInstance, field: 'email')}</a></td>
                        </tr>
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="entry.contactAddress.label" default="Contact Address" /></td>
                            <td valign="top" class="value">${fieldValue(bean: entryInstance, field: "contactAddress")}</td>
                        </tr>
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="entry.payment.label" default="Payment" /></td>
                            <td valign="top" class="value">${entryInstance?.payment?.encodeAsHTML()}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${entryInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
