

<%@ page import="tournament.Entry" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'entry.label', default: 'Entry')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:form action="apply" >
                <div class="dialog">
                <p>
                    Enter your team name, league &amp; division, and contact details here.  Most of the 
                    fields are mandatory.  Please ensure you check details carefully before submitting
                    the entry.
                </p>
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="club"><g:message code="entry.club.label" default="Club" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: entryInstance, field: 'club', 'errors')}">
                                    <strong>${clubInstance.name}</strong>
                                    <g:hiddenField name="club.id" value="${clubInstance.id}"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="ageGroup"><g:message code="entry.ageGroup.label" default="Age Group" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: entryInstance, field: 'ageGroup', 'errors')}">
                                    <g:select name="ageGroup" from="${entryInstance.constraints.ageGroup.inList}" value="${entryInstance?.ageGroup}" valueMessagePrefix="entry.ageGroup"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="teamName"><g:message code="entry.teamName.label" default="Team Name" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: entryInstance, field: 'teamName', 'errors')}">
                                    <g:textField name="teamName" value="${entryInstance?.teamName}" />
                                    <g:render template="/fieldError" model="['instance':entryInstance,'field':'teamName']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="league"><g:message code="entry.league.label" default="League" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: entryInstance, field: 'league', 'errors')}">
                                    <g:select name="league" from="${entryInstance.constraints.league.inList}" value="${entryInstance?.league}" valueMessagePrefix="entry.league"  />
                                    <g:render template="/fieldError" model="['instance':entryInstance,'field':'league']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="otherLeague"><g:message code="entry.otherLeague.label" default="Other League" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: entryInstance, field: 'otherLeague', 'errors')}">
                                    <g:textField name="otherLeague" value="${entryInstance?.otherLeague}" />
                                    <g:render template="/fieldError" model="['instance':entryInstance,'field':'otherLeague']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="division"><g:message code="entry.division.label" default="Division" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: entryInstance, field: 'division', 'errors')}">
                                    <g:textField name="division" value="${entryInstance?.division}" />
                                    <g:render template="/fieldError" model="['instance':entryInstance,'field':'division']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="contactName"><g:message code="entry.contactName.label" default="Contact Name" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: entryInstance, field: 'contactName', 'errors')}">
                                    <g:textField name="contactName" value="${entryInstance?.contactName}" />
                                    <g:render template="/fieldError" model="['instance':entryInstance,'field':'contactName']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="phoneNumber"><g:message code="entry.phoneNumber.label" default="Phone Number" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: entryInstance, field: 'phoneNumber', 'errors')}">
                                    <g:textField name="phoneNumber" value="${entryInstance?.phoneNumber}" />
                                    <g:render template="/fieldError" model="['instance':entryInstance,'field':'phoneNumber']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="mobileNumber"><g:message code="entry.mobileNumber.label" default="Mobile Number" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: entryInstance, field: 'mobileNumber', 'errors')}">
                                    <g:textField name="mobileNumber" value="${entryInstance?.mobileNumber}" />
                                    <g:render template="/fieldError" model="['instance':entryInstance,'field':'mobileNumber']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="email"><g:message code="entry.email.label" default="Email" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: entryInstance, field: 'email', 'errors')}">
                                    <g:textField name="email" value="${entryInstance?.email}" />
                                    <g:render template="/fieldError" model="['instance':entryInstance,'field':'email']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="contactAddress"><g:message code="entry.contactAddress.label" default="Contact Address" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: entryInstance, field: 'contactAddress', 'errors')}">
                                    <g:textArea name="contactAddress" value="${entryInstance?.contactAddress}" />
                                    <g:render template="/fieldError" model="['instance':entryInstance,'field':'contactAddress']"/>
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="submit" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
