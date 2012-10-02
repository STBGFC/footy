<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
        <title><g:message code="org.davisononline.org.footy.core.registration.validatetoken.title" default="Email Validation Token" /></title>
    </head>
    <body>
        <h1><g:message code="org.davisononline.org.footy.core.registration.validatetoken.title" default="Email Validation Token" /></h1>
        <div class="dialog">
            <g:form name="registration" action="registerPlayer">
                <p>
                   <g:message
                           code="org.davisononline.footy.core.registration.token.text"
                           default="An email containing a token has been sent to the address you entered.  Please enter that token here so that we know your email address works."/>
                </p>

                <table>
                    <tbody>
                        <tr class="prop">
                            <td  class="name">
                                <label for="token"><g:message code="org.davisononline.org.footy.core.registration.token.label" default="Token from your email" /></label>
                            </td>
                            <td  class="value">
                                <g:textField name="token" value="" />
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
