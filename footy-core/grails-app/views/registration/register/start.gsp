<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
        <title><g:message code="org.davisononline.org.footy.core.registration.start.title" default="Player Registration" /></title>
    </head>
    <body>
        <h1><g:message code="org.davisononline.org.footy.core.registration.start.title" default="Player Registration" /></h1>
        <div class="dialog">
            <g:form name="registration" action="register">
                <p>
                   <g:message
                           code="org.davisononline.footy.core.registrationemail.text"
                           default="First, please confirm your email address so that we can validate it (a working email address is an absolute requirement for player registration). Enter it CAREFULLY as we will send a token (password) to it which you must be able to receive in order to continue."/>
                </p>

                <p>
                    If you are already registered in the database, you MUST use the email address that we have stored for you.
                </p>

                <table>
                    <tbody>
                        <tr class="prop">
                            <td  class="name">
                                <label for="email"><g:message code="org.davisononline.org.footy.core.registrationemail.label" default="Your email address" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: person, field: 'email', 'errors')}">
                                <g:textField name="email" value="${person?.email}" />
                                <g:render template="/shared/fieldError" model="['instance':person,'field':'email']" plugin="footy-core"/>
                            </td>
                        </tr>
                    </tbody>
                </table>

                <div class="buttons flowcontrol">
                    <span class="button"><g:submitButton name="continue" class="save" value="${message(code: 'default.button.continue.label', default: 'Continue')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
