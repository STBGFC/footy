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
                        <g:render template="/playerFormBody"/>

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
        