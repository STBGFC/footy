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

      </div>

  </body>
</html>

