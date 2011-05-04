<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
        <title><g:message code="default.paymentdetails.label" default="${registration.tier.name} Registration" /></title>
        <script type="text/javascript">var focusField='person.givenName'</script>
    </head>
    <body>
        <div class="dialog">
            <g:form action="registerPlayer">
                <p><g:message code="org.davisononline.footy.registration.views.registration.registerPlayer.enterPlayerDetails.para1"
                        default="Enter details of the new player in the fields below." /></p>
                <table>
                    <tbody>    
                        <g:render template="/player/playerFormBody" plugin="footy-core"/>
                    </tbody>
                </table>
        
                <div class="buttons">
                    <span class="button"><g:submitButton name="submit" class="save" value="${message(code: 'default.button.continue.label', default: 'Continue')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
        