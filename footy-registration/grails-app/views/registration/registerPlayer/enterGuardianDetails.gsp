<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
        <title><g:message code="default.guardiandetails.label" default="Enter Parent/Guardian Details" /></title>
    </head>
    <body>
        <div class="dialog">
            <g:form action="registerPlayer">
                <p>The player is a minor: please enter details of the parent or guardian in the fields below.</p>
                             
                <table>
                    <tbody>    
                
                        <tr class="prop">
                            <td  class="name">
                                <label for="givenName"><g:message code="org.davisononline.org.footy.core.contactGivenName.label" default="Given Name" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: personCommand, field: 'givenName', 'errors')}">
                                <g:textField name="givenName" value="${personCommand?.givenName}" />
                                <g:render template="/fieldError" model="['instance':personCommand,'field':'givenName']" plugin="footy-core"/>
                            </td>
                        </tr>
                    
                        <tr class="prop">
                            <td  class="name">
                                <label for="familyName"><g:message code="org.davisononline.org.footy.core.contactFamilyName.label" default="Family Name" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: personCommand, field: 'familyName', 'errors')}">
                                <g:textField name="familyName" value="${personCommand?.familyName}" />
                                <g:render template="/fieldError" model="['instance':personCommand,'field':'familyName']" plugin="footy-core"/>
                            </td>
                        </tr>
                        
                        <tr class="prop">
                            <td  class="name">
                                <label for=phone1><g:message code="org.davisononline.org.footy.core.phone1.label" default="Contact Tel." /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: personCommand, field: 'phone1', 'errors')}">
                                <g:textField name="phone1" value="${personCommand?.phone1}" />
                                <g:render template="/fieldError" model="['instance':personCommand,'field':'phone1']" plugin="footy-core"/>
                            </td>
                        </tr>
                    
                        <tr class="prop">
                            <td  class="name">
                                <label for="email"><g:message code="org.davisononline.org.footy.core.email.label" default="Email" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: personCommand, field: 'email', 'errors')}">
                                <g:textField name="email" value="${personCommand?.email}" />
                                <g:render template="/fieldError" model="['instance':personCommand,'field':'email']" plugin="footy-core"/>
                            </td>
                        </tr>
                        
                        <tr class="prop">
                            <td  class="name">
                                <label for="address"><g:message code="org.davisononline.org.footy.core.address.label" default="Home Address" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: personCommand, field: 'address', 'errors')}">
                                <g:textArea name="address" value="${personCommand?.address}" rows="4" cols="30"/>
                                <g:render template="/fieldError" model="['instance':personCommand,'field':'address']" plugin="footy-core"/>
                            </td>
                        </tr>
                
                    </tbody>
                </table>
                
                <div class="buttons">
                    <span class="button"><g:submitButton name="continue" class="save" value="${message(code: 'default.button.continue.label', default: 'Continue')}" /></span>
                    <g:if test="${!guardian1}"><span class="button"><g:submitButton name="addanother" class="save" value="${message(code: 'org.davisononline.footy.registration.addotherguardian.button.label', default: 'Submit and add another parent/guardian')}" /></span></g:if>
                </div>
            </g:form>
        </div>
    </body>
</html>
        