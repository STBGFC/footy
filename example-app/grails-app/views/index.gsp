<%@ page import="org.davisononline.footy.core.Team; org.davisononline.footy.tournament.Tournament; org.davisononline.footy.core.Club" %>
<html>
  <head>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	  <title>Example FC Registrations and Payments</title>
  </head>

  <body>
      <div id="homemain">
          <g:if test="${Tournament.countByOpenForEntry(true) > 0}">
          <p>
              <g:each in="${Tournament.findAllByOpenForEntry(true)}" var="tourney">
                  <g:link controller="entry" action="apply" id="${tourney.id}">Enter some teams into the ${tourney.name}</g:link>
              </g:each>
          </p>
          </g:if>
      </div>

      <div id="newspanel">
          <div class="newsbox">
              <p>Welcome to our club!</p>
              <g:link controller="registration">Register Player</g:link>
          </div>

          <sec:ifAnyGranted roles="ROLE_SYSADMIN,ROLE_CLUB_ADMIN">
          <div class="newsbox">
              <h2>Club Administration</h2>
              <ul>
                  <li>
                      <g:link controller="person" action="list">People</g:link>
                  </li>
                  <li>
                      <g:link controller="player" action="list">Players</g:link>
                  </li>
                  <li>
                      <g:link controller="team" action="list">Teams</g:link>
                  </li>
                  <li>
                      <g:link controller="person" action="qualifications">Qualifications expiring soon</g:link>
                  </li>
                  <li>
                      <g:link controller="invoice" action="list">Payment Reconciliations</g:link>
                  </li>
                  <li>
                      <g:link controller="registrationTier" action="list">Registration tiers</g:link>
                  </li>
                  <li>
                      <g:link controller="qualificationType" action="list">Qualification types</g:link>
                  </li>
              </ul>
          </div>
          </sec:ifAnyGranted>

          <sec:ifAnyGranted roles="ROLE_TOURNAMENT_ADMIN">
          <div class="newsbox">
              <h2>Tournament Administration</h2>
              <ul>
                  <li>
                      <g:link controller="tournament" action="list">All Tournaments</g:link>
                  </li>
              </ul>
          </div>
          </sec:ifAnyGranted>

      </div>

  </body>
</html>

