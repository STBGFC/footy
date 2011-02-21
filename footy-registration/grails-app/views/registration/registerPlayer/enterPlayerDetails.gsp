<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
        <title><g:message code="default.paymentdetails.label" default="Enter Player Details" /></title>
    </head>
    <body>
        <div class="dialog">
            <g:form action="registerPlayer">
                <p>Enter details of the new player in the fields below.</p>
                
                <table>
                    <tbody>    
                
                        <tr class="prop">
                            <td  class="name">
                                <label for="contactName"><g:message code="entry.contactGivenName.label" default="Given Name" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: playerInstance, field: 'givenName', 'errors')}">
                                <g:textField name="givenName" value="${playerInstance?.givenName}" />
                                <g:render template="/fieldError" model="['instance':playerInstance,'field':'givenName']" plugin="footy-core"/>
                            </td>
                        </tr>
                    
                        <tr class="prop">
                            <td  class="name">
                                <label for="familyName"><g:message code="entry.contactFamilyName.label" default="Family Name" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: playerInstance, field: 'familyName', 'errors')}">
                                <g:textField name="familyName" value="${playerInstance?.familyName}" />
                                <g:render template="/fieldError" model="['instance':playerInstance,'field':'familyName']" plugin="footy-core"/>
                            </td>
                        </tr>
                    
                        <tr class="prop">
                            <td  class="name">
                                <label for="knownAsName"><g:message code="entry.contactKnownAsName.label" default="Known As" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: playerInstance, field: 'knownAsName', 'errors')}">
                                <g:textField name="familyName" value="${playerInstance?.knownAsName}" />
                                <g:render template="/fieldError" model="['instance':playerInstance,'field':'knownAsName']" plugin="footy-core"/>
                            </td>
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">
                              <label for="dob"><g:message code="registration.playerDob.label" default="Date of Birth" /></label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean: playerInstance, field: 'dob', 'errors')}">
                                <g:datePicker name="dob" precision="day" value="${playerInstance?.dob}"  />
                            </td>
                        </tr>
                    
                    </tbody>
                </table>
        
                <div class="buttons">
                    <span class="button"><g:submitButton name="submit" class="save" value="${message(code: 'default.button.continue.label', default: 'Continue')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
        