<%@ page import="org.davisononline.footy.core.*" %>
    <g:form name="addQualification">
        <g:hiddenField name="personId" value="${personId}"/>
        <p>You can add one or more qualifications for this person here.</p>
        <table>
            <tbody>
                <tr class="prop">
                    <td  class="name">
                        <label for="type.id"><g:message code="org.davisononline.footy.core.qualificationtype.label" default="Qualification" /></label>
                    </td>
                    <td  class="value">
                        <g:select name="type.id" from="${qualificationTypes}" optionKey="id" optionValue="name"/>
                    </td>
                </tr>
                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="attainedOn"><g:message code="org.davisononline.footy.core.qualificationattained.label" default="Date Attained" /></label>
                    </td>
                    <td valign="top" class="value date">
                        <g:datePicker name="attainedOn" precision="day" years="${(new Date().year-20+1900)..(new Date().year+1900)}"/>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td class="value date"><g:submitToRemote value="Add" title="Add" update="qualifications" url="[controller:'person',action:'addQualification']"/></td>
                </tr>
            </tbody>
        </table>
    </g:form>
