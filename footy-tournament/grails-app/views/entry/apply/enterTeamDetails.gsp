<%@ page import="org.davisononline.footy.tournament.Entry" %>
<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
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
                                <td  class="value ${hasErrors(bean: teamInstance, field: 'club', 'errors')}">
                                    <strong>${clubInstance.name}</strong>
                                    <g:hiddenField name="club.id" value="${clubInstance.id}"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="ageBand"><g:message code="entry.ageBand.label" default="Age Group" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: teamInstance, field: 'ageBand', 'errors')}">
                                    Under: <g:select name="ageBand" from="${teamInstance.constraints.ageBand.inList}" value="${teamInstance?.ageBand}" valueMessagePrefix="entry.ageBand"  />
                                    <g:checkBox name="girlsTeam" value="${teamInstance?.girlsTeam}" /> (Girls)
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="teamName"><g:message code="entry.teamName.label" default="Team Name" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: teamInstance, field: 'teamName', 'errors')}">
                                    <g:textField name="teamName" value="${teamInstance?.teamName}" />
                                    <g:render template="/fieldError" model="['instance':teamInstance,'field':'teamName']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="league"><g:message code="entry.league.label" default="League" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: teamInstance, field: 'league.id', 'errors')}">
                                    <g:select name="league" from="${League.list()}" value="${teamInstance?.league}" valueMessagePrefix="entry.league"  />
                                    <g:render template="/fieldError" model="['instance':teamInstance,'field':'league.id']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="division"><g:message code="entry.division.label" default="Division" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: teamInstance, field: 'division', 'errors')}">
                                    <g:textField name="division" value="${teamInstance?.division}" />
                                    <g:render template="/fieldError" model="['instance':teamInstance,'field':'division']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="contactName"><g:message code="entry.contactName.label" default="Contact Name" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: teamInstance, field: 'contactName', 'errors')}">
                                    <g:textField name="contactName" value="${teamInstance?.contactName}" />
                                    <g:render template="/fieldError" model="['instance':teamInstance,'field':'contactName']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="phoneNumber"><g:message code="entry.phoneNumber.label" default="Phone Number" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: teamInstance, field: 'phoneNumber', 'errors')}">
                                    <g:textField name="phoneNumber" value="${teamInstance?.phoneNumber}" />
                                    <g:render template="/fieldError" model="['instance':teamInstance,'field':'phoneNumber']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="mobileNumber"><g:message code="entry.mobileNumber.label" default="Mobile Number" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: teamInstance, field: 'mobileNumber', 'errors')}">
                                    <g:textField name="mobileNumber" value="${teamInstance?.mobileNumber}" />
                                    <g:render template="/fieldError" model="['instance':teamInstance,'field':'mobileNumber']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="email"><g:message code="entry.email.label" default="Email" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: teamInstance, field: 'email', 'errors')}">
                                    <g:textField name="email" value="${teamInstance?.email}" />
                                    <g:render template="/fieldError" model="['instance':teamInstance,'field':'email']"/>
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
