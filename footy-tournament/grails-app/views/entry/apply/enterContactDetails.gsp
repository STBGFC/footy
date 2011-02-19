<%@ page import="org.davisononline.footy.tournament.Entry" %>
<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title><g:message code="enter.contactdetails.label" default="${entryInstance.tournament.name} Tournament" /></title>
    </head>
    <body>
        <div class="dialog">
            <g:form action="apply" >
                <h2>Welcome to our tournament entry application.</h2>
                <p>
                    Please start by completing your own contact details below.
                </p>
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="givenName"><g:message code="entry.contactGivenName.label" default="Given Name" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: personInstance, field: 'givenName', 'errors')}">
                                    <g:textField name="givenName" value="${personInstance?.givenName}" />
                                    <g:render template="/fieldError" model="['instance':personInstance,'field':'givenName']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="familyName"><g:message code="entry.contactFamilyName.label" default="Family Name" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: personInstance, field: 'familyName', 'errors')}">
                                    <g:textField name="familyName" value="${personInstance?.familyName}" />
                                    <g:render template="/fieldError" model="['instance':personInstance,'field':'familyName']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for=phone1><g:message code="entry.contactPhone.label" default="Contact Tel." /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: personInstance, field: 'phone1', 'errors')}">
                                    <g:textField name="phone1" value="${personInstance?.phone1}" />
                                    <g:render template="/fieldError" model="['instance':personInstance,'field':'phone1']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="email"><g:message code="entry.email.label" default="Email" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: personInstance, field: 'email', 'errors')}">
                                    <g:textField name="email" value="${personInstance?.email}" />
                                    <g:render template="/fieldError" model="['instance':personInstance,'field':'email']"/>
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                <div class="buttons">
                    <span class="button"><g:submitButton name="submit" class="save" value="${message(code: 'button.contactDetails.submit.label', default: 'Continue...')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
