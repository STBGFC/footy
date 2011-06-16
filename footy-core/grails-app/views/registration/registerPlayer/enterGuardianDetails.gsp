<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
        <title><g:message code="default.guardiandetails.label" default="Enter Parent/Guardian Details" /></title>
        <script type="text/javascript">var focusField='givenName'</script>
    </head>
    <body>
        <div class="dialog">
            <g:form name="registration" action="registerPlayer">
                <p>
                    <g:message code="org.davisononline.footy.registration.views.registration.registerPlayer.para1"
                        default="The player is a minor: please enter details of the parent or guardian in the fields below." />
                </p>
                <g:if test="${!guardian1}">
                <p>
                    <g:message code="org.davisononline.footy.registration.views.registration.registerPlayer.para2"
                        default="Once entered, you can choose to continue or submit and then add a second parent or guardian.  Click the appropriate button after filling in the form." />
                </p>
                </g:if>
                             
                <table>
                    <tbody>
                        <g:render template="/person/personFormBody" plugin="footy-core"/>
                    </tbody>
                </table>
                
                <div class="buttons flowcontrol">
                    <span class="button"><g:submitButton name="continue" class="save" value="${message(code: 'default.button.continue.label', default: 'Continue')}" /></span>
                    <g:if test="${!guardian1}"><span class="button"><g:submitButton name="addanother" class="save" value="${message(code: 'org.davisononline.footy.registration.addotherguardian.button.label', default: 'Add another parent...')}" /></span></g:if>
                </div>
            </g:form>
        </div>
    </body>
</html>
        
