<%@ page import="org.davisononline.footy.tournament.Entry" %>
<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title><g:message code="enter.teamdetails.label" default="Enter Team Details" /></title>
    </head>
    <body>
        <div class="dialog">
            <g:form action="apply" >
                <p>
                    Enter your team name, league &amp; division, and contact details here.  Most of the 
                    fields are mandatory.  Please ensure you check details carefully before submitting
                    the entry.
                </p>
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="club"><g:message code="entry.club.label" default="Club" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: teamCommand, field: 'club', 'errors')}">
                                    <strong>${clubInstance.name}</strong>
                                    <g:hiddenField name="club.id" value="${clubInstance.id}"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="ageBand"><g:message code="entry.ageBand.label" default="Age Group" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: teamCommand, field: 'ageBand', 'errors')}">
                                    Under: <g:select class="short" name="ageBand" from="${(7..18).toList()}" value="${teamCommand?.ageBand}" valueMessagePrefix="entry.ageBand"  />
                                    <g:checkBox name="girlsTeam" value="${teamCommand?.girlsTeam}" /> (Girls)
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="name"><g:message code="entry.teamName.label" default="Team Name" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: teamCommand, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${teamCommand?.name}" />
                                    <g:render template="/fieldError" model="['instance':teamCommand,'field':'name']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="league"><g:message code="entry.league.label" default="League" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: teamCommand, field: 'leagueId', 'errors')}">
                                    <g:select name="leagueId" from="${League.list()}" value="${teamCommand?.leagueId}" optionValue="name" optionKey="id" valueMessagePrefix="entry.league"  />
                                    <g:render template="/fieldError" model="['instance':teamCommand,'field':'leagueId']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="division"><g:message code="entry.division.label" default="Division" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: teamCommand, field: 'division', 'errors')}">
                                    <g:textField name="division" value="${teamCommand?.division}" />
                                    <g:render template="/fieldError" model="['instance':teamCommand,'field':'division']"/>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                <div class="buttons">
                    <span class="button"><g:submitButton name="submit" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
