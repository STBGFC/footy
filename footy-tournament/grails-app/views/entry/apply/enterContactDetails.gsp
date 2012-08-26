<%@ page import="org.davisononline.footy.tournament.Entry" %>
<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title><g:message code="org.davisononline.footy.tournament.views.entry.apply.title" default="${entryInstance.tournament.name} Entry" /></title>
    </head>
    <body>
        <h1><g:message code="org.davisononline.footy.tournament.views.entry.apply.title" default="${entryInstance.tournament.name} Entry" /></h1>
        <div class="dialog">
            <g:form action="apply" >
                <p>
                    You are creating team entries for our <strong>${entryInstance.tournament.name}</strong>
                    which is ${entryInstance.tournament.startDate == entryInstance.tournament.endDate ? "on " + formatDate(date:entryInstance.tournament.startDate, format:"dd MMMM yyyy") : "from " + formatDate(date:entryInstance.tournament.startDate, format:"dd-") + formatDate(date:entryInstance.tournament.endDate, format: "dd MMMM yyyy")}
                </p>
                <p>
                    Please start by completing your own contact details below.
                </p>
                    <table>
                        <tbody>
                        
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
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="email"><g:message code="org.davisononline.footy.tournament.views.entry.apply.email.label" default="Email" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: personInstance, field: 'email', 'errors')}">
                                    <g:textField name="email" value="${personInstance?.email}" />
                                    <g:render template="/shared/fieldError" model="['instance':personInstance,'field':'email']" plugin="footy-core"/>
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
