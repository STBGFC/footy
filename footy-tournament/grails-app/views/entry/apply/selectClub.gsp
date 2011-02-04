<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title><g:message code="select.club.heading" default="Select Club" /></title>
    </head>

    <body>
        <g:form action="apply">
        <p>
            First, select the club that your team is a part of from the list
            below.  If your club is not shown, you will need to add it by
            clicking the "Add new club..." link - be aware that you will 
            need the details of your club's secretary and league affiliation
            details to do this.
        </p>
        <table>
        <tbody>
            <tr class="prop">
            <td  class="name">
                <label for="club"><g:message code="club.select.label" default="Club" /></label>
            </td>
            <td  class="value">
            <g:select 
                name="club.id" 
                from="${Club.list(sort:'name')}" 
                optionKey="id"
                optionValue="name"
                />
            </td>
            </tr>
            <tr><td>&nbsp;</td><td>
            <g:link action="apply" event="createNew">Add new club...</g:link>
            </td>
            </tr>
        </tbody>
        </table>
        <div class="buttons">
            <span class="button"><g:submitButton name="selected" class="save" value="Continue.."></g:submitButton></span>
        </div>
        </g:form>
    </body>
</html>

