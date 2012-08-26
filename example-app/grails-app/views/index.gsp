<%@ page import="org.davisononline.footy.core.*; org.davisononline.footy.tournament.Tournament; org.davisononline.footy.core.Club" %>
<html>
  <head>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	  <title>Example FC Registrations and Payments</title>
  </head>

  <body>
      <div id="homemain">
          <h1>Example App - Intro</h1>
          <p>Welcome to our club!</p>
          <p>This is an example application, you can try registering some new players 
          using the <g:link controller="registration">Register Player</g:link> link, or
          try logging in as one of the users that are set up with various roles.</p>
          <ul>
            <li>Manager1 : Manager1</li>
            <li>clubAdmin : clubAdmin1</li>
            <li>officer : Officer1</li>
          </ul>

          <h1>Tournaments</h1>
          <p>If any tournaments are created and open for entry, you will see links to 
          use them below:</p>          
          <g:if test="${Tournament.countByOpenForEntry(true) > 0}">
          <p>
              <g:each in="${Tournament.findAllByOpenForEntry(true)}" var="tourney">
                  <g:link controller="entry" action="apply" id="${tourney.id}">Enter some teams into the ${tourney.name}</g:link>
              </g:each>
          </p>
          </g:if>

          <h1>Contribute Back</h1>
          <p>Have fun, and please <a href="http://github.com/davison/footy">fork the source repo at GitHub</a>
          in order to contribute any improvements or patches you have!</p>
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

