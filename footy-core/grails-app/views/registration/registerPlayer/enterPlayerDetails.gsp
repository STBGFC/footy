<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
        <title><g:message code="default.paymentdetails.label" default="New Player Registration" /></title>
    </head>
    <body>
        <div class="dialog">
            <g:form action="registerPlayer">
                <p><g:message code="org.davisononline.footy.registration.views.registration.registerPlayer.enterPlayerDetails.para1"
                        default="Enter details of the new player in the fields below." /></p>
                
                <table>
                    <tbody>    
                
                        <tr class="prop">
                            <td  class="name">
                                <label for="givenName"><g:message code="org.davisononline.org.footy.core.contactGivenName.label" default="Given Name" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: playerCommand, field: 'givenName', 'errors')}">
                                <g:textField name="givenName" value="${playerCommand?.givenName}" />
                                <g:render template="/fieldError" model="['instance':playerCommand,'field':'givenName']"/>
                            </td>
                        </tr>
                    
                        <tr class="prop">
                            <td  class="name">
                                <label for="familyName"><g:message code="org.davisononline.org.footy.core.contactFamilyName.label" default="Family Name" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: playerCommand, field: 'familyName', 'errors')}">
                                <g:textField name="familyName" value="${playerCommand?.familyName}" />
                                <g:render template="/fieldError" model="['instance':playerCommand,'field':'familyName']"/>
                            </td>
                        </tr>
                    
                        <tr class="prop">
                            <td  class="name">
                                <label for="knownAsName"><g:message code="org.davisononline.org.footy.core.contactKnownAsName.label" default="Known As" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: playerCommand, field: 'knownAsName', 'errors')}">
                                <g:textField name="knownAsName" value="${playerCommand?.knownAsName}" />
                                <g:render template="/fieldError" model="['instance':playerCommand,'field':'knownAsName']"/>
                            </td>
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">
                              <label for="dob"><g:message code="org.davisononline.org.footy.registration.playerDob.label" default="Date of Birth" /></label>
                            </td>
                            <td valign="top" class="value date">
                                <g:datePicker name="dob" precision="day" years="${(new Date().year-19+1900)..(new Date().year-5+1900)}" value="${playerCommand?.dob}"  />
                            </td>
                        </tr>

                        <tr class="prop">
                            <td  class="name">
                                <label for="parentId"><g:message code="org.davisononline.org.footy.core.playerGuardian.label" default="Parent/Guardian" /></label>
                            </td>
                            <td  class="value">
                                <g:select name="parentId" from="${Person.findAllEligibleParent()}" noSelection="[null:'-- Not listed or not applicable --']" optionKey="id"/>
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
        