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
                <p>
                    If you are creating a "Parents/Vets" team, then you will still be forced to choose an
                    age band, a league and a division, but this information will be ignored by the system.
                </p>
                    <table>
                        <tbody>
                            <g:render template="/team/teamFormBody" plugin="footy-core"/>
                        </tbody>
                    </table>
                <div class="buttons">
                    <span class="button"><g:submitButton name="submit" class="save" value="${message(code: 'team.button.submit.label', default: 'Submit Team')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
