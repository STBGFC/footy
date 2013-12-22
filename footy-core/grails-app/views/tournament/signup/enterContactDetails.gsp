<%@ page import="org.davisononline.footy.tournament.Entry" %>
<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title><g:message code="org.davisononline.footy.tournament.views.entry.apply.title" default="${tournament.name} Entry" /></title>
    </head>
    <body>
        <h1><g:message code="org.davisononline.footy.tournament.views.entry.apply.title" default="${tournament.name} Entry" /></h1>
        <div class="dialog">
            <g:form action="signup">
                <p>
                    <g:message code="org.davisononline.footy.tournament.views.entry.apply.text"
                        default="Please enter your name and phone number.." />
                </p>
                    <table>
                        <tbody>

                            <tr class="prop">
                                <td  class="name">
                                    <g:message code="org.davisononline.footy.tournament.views.entry.apply.email.label" default="Email" />
                                </td>
                                <td  class="value">
                                    ${personInstance?.email ?: email}
                                    <g:hiddenField name="email" value="${personInstance?.email ?: email}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="givenName"><g:message code="org.davisononline.footy.tournament.views.entry.apply.contactGivenName.label" default="First (Given) Name" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: personInstance, field: 'givenName', 'errors')}">
                                    <g:textField name="givenName" value="${personInstance?.givenName}" />
                                    <g:render template="/shared/fieldError" model="['instance':personInstance,'field':'givenName']" plugin="footy-core"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="familyName"><g:message code="org.davisononline.footy.tournament.views.entry.apply.contactFamilyName.label" default="Last (Family) Name" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: personInstance, field: 'familyName', 'errors')}">
                                    <g:textField name="familyName" value="${personInstance?.familyName}" />
                                    <g:render template="/shared/fieldError" model="['instance':personInstance,'field':'familyName']" plugin="footy-core"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for=phone1><g:message code="org.davisononline.footy.tournament.views.entry.apply.contactPhone.label" default="Contact Tel." /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: personInstance, field: 'phone1', 'errors')}">
                                    <g:textField name="phone1" value="${personInstance?.phone1}" />
                                    <g:render template="/shared/fieldError" model="['instance':personInstance,'field':'phone1']" plugin="footy-core"/>
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
