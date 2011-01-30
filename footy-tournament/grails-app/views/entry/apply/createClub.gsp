

<%@ page import="tournament.Club" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
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
                    Enter the details for your club below, including the affiliation county and
                    number if you have them.
                </p>
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="name"><g:message code="club.name.label" default="Name" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: clubInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${clubInstance?.name}" />
                                    <g:render template="/fieldError" model="['instance':clubInstance,'field':'name']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="colours"><g:message code="club.colours.label" default="Colours" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: clubInstance, field: 'colours', 'errors')}">
                                    <g:textField name="colours" value="${clubInstance?.colours}" />
                                    <g:render template="/fieldError" model="['instance':clubInstance,'field':'colours']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="clubSecretaryName"><g:message code="club.clubSecretaryName.label" default="Club Secretary Name" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: clubInstance, field: 'clubSecretaryName', 'errors')}">
                                    <g:textField name="clubSecretaryName" value="${clubInstance?.clubSecretaryName}" />
                                    <g:render template="/fieldError" model="['instance':clubInstance,'field':'clubSecretaryName']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="clubSecretaryAddress"><g:message code="club.clubSecretaryAddress.label" default="Club Secretary Address" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: clubInstance, field: 'clubSecretaryAddress', 'errors')}">
                                    <g:textArea name="clubSecretaryAddress" value="${clubInstance?.clubSecretaryAddress}" />
                                    <g:render template="/fieldError" model="['instance':clubInstance,'field':'clubSecretaryAddress']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="countyAffiliatedTo"><g:message code="club.countyAffiliatedTo.label" default="County Affiliated To" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: clubInstance, field: 'countyAffiliatedTo', 'errors')}">
                                    <g:textField name="countyAffiliatedTo" value="${clubInstance?.countyAffiliatedTo}" />
                                    <g:render template="/fieldError" model="['instance':clubInstance,'field':'countyAffiliatedTo']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="countyAffiliationNumber"><g:message code="club.countyAffiliationNumber.label" default="County Affiliation Number" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: clubInstance, field: 'countyAffiliationNumber', 'errors')}">
                                    <g:textField name="countyAffiliationNumber" value="${clubInstance?.countyAffiliationNumber}" />
                                    <g:render template="/fieldError" model="['instance':clubInstance,'field':'countyAffiliationNumber']"/>
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
