<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
        <title><g:message code="default.registrationtype.label" default="Registration Type" /></title>
    </head>
    <body>
        <div class="dialog">
            <g:form action="registerPlayer">
                <p>
                    Please select the registration type (your coach or manager can advise you if you're unsure which one to pick)
                </p>

                <table>
                    <tbody>
                        <tr class="prop">
                            <td  class="name">
                                <label for="regTierId"><g:message code="org.davisononline.org.footy.core.registration.label" default="Registration Type" /></label>
                            </td>
                            <td  class="value">
                                <g:select name="regTierId" from="${RegistrationTier.list()}" optionKey="id"/>
                            </td>
                        </tr>
                    </tbody>
                </table>

                <div class="buttons">
                    <span class="button"><g:submitButton name="continue" class="save" value="${message(code: 'default.button.continue.label', default: 'Continue')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
