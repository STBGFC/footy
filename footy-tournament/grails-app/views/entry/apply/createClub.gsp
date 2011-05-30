<%@ page import="org.davisononline.footy.core.Club" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="dialog">
            <g:form action="apply" >
                <p>
                    Enter the details for your club below, including the affiliation county and number if you have them.
                </p>
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="name"><g:message code="club.name.label" default="Name" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: clubCommand, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${clubCommand?.name}" />
                                    <g:render template="/shared/fieldError" model="['instance':clubCommand,'field':'name']" plugin="footy-core"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="colours"><g:message code="club.colours.label" default="Colours" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: clubCommand, field: 'colours', 'errors')}">
                                    <g:textField name="colours" value="${clubCommand?.colours}" />
                                    <g:render template="/shared/fieldError" model="['instance':clubCommand,'field':'colours']" plugin="footy-core"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="clubSecretaryName"><g:message code="club.clubSecretaryName.label" default="Club Secretary Name" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: clubCommand, field: 'clubSecretaryName', 'errors')}">
                                    <g:textField name="clubSecretaryName" value="${clubCommand?.clubSecretaryName}" />
                                    <g:render template="/shared/fieldError" model="['instance':clubCommand,'field':'clubSecretaryName']" plugin="footy-core"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="clubSecretaryEmail"><g:message code="club.clubSecretaryEmail.label" default="Club Secretary Email" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: clubCommand, field: 'clubSecretaryEmail', 'errors')}">
                                    <g:textField name="clubSecretaryEmail" value="${clubCommand?.clubSecretaryEmail}" />
                                    <g:render template="/shared/fieldError" model="['instance':clubCommand,'field':'clubSecretaryEmail']" plugin="footy-core"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td  class="name">
                                    <label for="clubSecretaryPhone"><g:message code="club.clubSecretaryPhone.label" default="Club Secretary Phone" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: clubCommand, field: 'clubSecretaryPhone', 'errors')}">
                                    <g:textField name="clubSecretaryPhone" value="${clubCommand?.clubSecretaryPhone}" />
                                    <g:render template="/shared/fieldError" model="['instance':clubCommand,'field':'clubSecretaryPhone']" plugin="footy-core"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="clubSecretaryAddress.house"><g:message code="club.clubSecretaryAddress.label" default="Club Secretary Address" /></label>
                                </td>
                            <td  class="value">
                                House number or name:<br/>
                                <g:textField name="clubSecretaryAddress.house" value="${clubCommand?.clubSecretaryAddress?.house}" />
                                <g:render template="/shared/fieldError" model="['instance':clubCommand?.clubSecretaryAddress,'field':'house']" plugin="footy-core"/>
                                <br/>Street:<br/>
                                <g:textField name="clubSecretaryAddress.address" value="${clubCommand?.clubSecretaryAddress?.address}" />
                                <g:render template="/shared/fieldError" model="['instance':clubCommand?.clubSecretaryAddress,'field':'address']" plugin="footy-core"/>
                                <br/>Town:<br/>
                                <g:textField name="clubSecretaryAddress.town" value="${clubCommand?.clubSecretaryAddress?.town}" />
                                <g:render template="/shared/fieldError" model="['instance':clubCommand?.clubSecretaryAddress,'field':'town']" plugin="footy-core"/>
                                <br/>Post Code:<br/>
                                <g:textField name="clubSecretaryAddress.postCode" value="${clubCommand?.clubSecretaryAddress?.postCode}" />
                                <g:render template="/shared/fieldError" model="['instance':clubCommand?.clubSecretaryAddress,'field':'postCode']" plugin="footy-core"/>
                            </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="countyAffiliatedTo"><g:message code="club.countyAffiliatedTo.label" default="County Affiliated To" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: clubCommand, field: 'countyAffiliatedTo', 'errors')}">
                                    <g:textField name="countyAffiliatedTo" value="${clubCommand?.countyAffiliatedTo}" />
                                    <g:render template="/shared/fieldError" model="['instance':clubCommand,'field':'countyAffiliatedTo']" plugin="footy-core"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="countyAffiliationNumber"><g:message code="club.countyAffiliationNumber.label" default="County Affiliation Number" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: clubCommand, field: 'countyAffiliationNumber', 'errors')}">
                                    <g:textField name="countyAffiliationNumber" value="${clubCommand?.countyAffiliationNumber}" />
                                    <g:render template="/shared/fieldError" model="['instance':clubCommand,'field':'countyAffiliationNumber']" plugin="footy-core"/>
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                <div class="buttons">
                    <span class="button"><g:submitButton name="submit" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
