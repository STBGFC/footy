<%@ page import="org.davisononline.footy.core.Club" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
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
                ${clubCommand}
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="name"><g:message code="club.name.label" default="Name" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: clubCommand, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${clubCommand?.name}" />
                                    <g:render template="/fieldError" model="['instance':clubCommand,'field':'name']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="colours"><g:message code="club.colours.label" default="Colours" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: clubCommand, field: 'colours', 'errors')}">
                                    <g:textField name="colours" value="${clubCommand?.colours}" />
                                    <g:render template="/fieldError" model="['instance':clubCommand,'field':'colours']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="clubSecretaryName"><g:message code="club.clubSecretaryName.label" default="Club Secretary Name" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: clubCommand, field: 'clubSecretaryName', 'errors')}">
                                    <g:textField name="clubSecretaryName" value="${clubCommand?.clubSecretaryName}" />
                                    <g:render template="/fieldError" model="['instance':clubCommand,'field':'clubSecretaryName']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="clubSecretaryAddress"><g:message code="club.clubSecretaryAddress.label" default="Club Secretary Address" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: clubCommand, field: 'clubSecretaryAddress', 'errors')}">
                                    <g:textArea name="clubSecretaryAddress" value="${clubCommand?.clubSecretaryAddress}" />
                                    <g:render template="/fieldError" model="['instance':clubCommand,'field':'clubSecretaryAddress']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="countyAffiliatedTo"><g:message code="club.countyAffiliatedTo.label" default="County Affiliated To" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: clubCommand, field: 'countyAffiliatedTo', 'errors')}">
                                    <g:textField name="countyAffiliatedTo" value="${clubCommand?.countyAffiliatedTo}" />
                                    <g:render template="/fieldError" model="['instance':clubCommand,'field':'countyAffiliatedTo']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="countyAffiliationNumber"><g:message code="club.countyAffiliationNumber.label" default="County Affiliation Number" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: clubCommand, field: 'countyAffiliationNumber', 'errors')}">
                                    <g:textField name="countyAffiliationNumber" value="${clubCommand?.countyAffiliationNumber}" />
                                    <g:render template="/fieldError" model="['instance':clubCommand,'field':'countyAffiliationNumber']"/>
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
