<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title><g:message code="org.davisononline.footy.tournament.views.entry.apply.confirmentry.title" default="Confirm Entry" /></title>
    </head>

    <body>
        <h1><g:message code="org.davisononline.footy.tournament.views.entry.apply.confirmentry.title" default="Confirm Entry" /></h1>
        <div class="dialog">
        <p>
            The following teams are ready to be entered.  Please choose whether to add more
            teams or proceed to payment. You can use the "back" button in your browser if you
            wish to amend the most recent team added to this list.
        </p>
        <p>
            Once you move to the payment screen, <em>you will no longer be able to go back and make changes</em>.
        </p>

        <g:form action="signup">
        <ol id="confirmEntryList" >
        <g:each in="${entries}" var="entry" status="i">
            <li>
                ${entry.clubAndTeam} will be entered into the ${entry.competition}
                ${entry.competition.entered.size() >= entry.competition.teamLimit ? "waiting list" : "competition"}
            </li>
        </g:each>       
        </ol>

        <p>
            Team Contact: <strong>${personInstance} (${personInstance.phone1})  ${personInstance.email}</strong>
        </p>
        <p>
            Please note, that if you pay online on the following screen, your place in the
            tournament will be much safer than if you don't.  Any payments made online will
            be refunded online in the event that the tournament or competition is cancelled
            or your team was unable to be included.
        </p>
        <div class="buttons">
            <span class="button"><g:submitButton name="createMore" class="create" value="Add More Teams"></g:submitButton></span>
            <span class="button"><g:submitButton name="submit" class="save" value="Continue to Payment"></g:submitButton></span>
        </div>
        </g:form>
        </div>
    </body>
</html>
