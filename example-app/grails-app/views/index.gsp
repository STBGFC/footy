<%@ page import="org.davisononline.footy.core.*; org.davisononline.footy.tournament.Tournament; org.davisononline.footy.core.Club" %>
<html>
  <head>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	  <title>Example FC Registrations and Payments</title>
  </head>

  <body>
      <div id="homemain">
          <p>Welcome to our club!</p>
          <g:link controller="registration">Register Player</g:link>
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
          <%-- add home page news here..--%>
          <g:each in="${NewsItem.findAllByClubWide(true)}" var="ni">
              <div class="newsItem">
                  <h2>${ni.subject}</h2>
                  <p>${ni.body}</p>
              </div>
          </g:each>
          </div>

      </div>

  </body>
</html>

