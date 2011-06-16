<%@ page import="org.davisononline.footy.core.Team; org.davisononline.footy.tournament.Tournament; org.davisononline.footy.core.Club" %>
<html>
  <head>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	  <title>STBGFC Registrations and Payments</title>
  </head>

  <body>
      <div id="homemain">
          <p>
              <g:link controller="registration" title="register now">
                  <img src="${createLinkTo(dir:'images', file:'registernow.png')}" alt="register now"/>
              </g:link>
          </p>
          <p>
              Registration is now open for the 2011-2012 season.
          </p>
          <p>
              <em>(NB: this is an online registration and payment form and <strong>NOT</strong> an enquiry
              form.  If you are enquiring about joining the club, please use
              <a href="http://www.stbgfc.co.uk/about/contact.php">this form</a> instead).</em>
          </p>
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
              <h2>${Club.homeClub.name} Team List</h2>
              <ul>
                  <g:set var="allTeams" value="${Team.findAllByClub(Club.homeClub, [sort:'ageBand', order:'asc'])}"/>
                  <li>Pre-League ::
                  <g:each in="${allTeams.grep{it.ageBand<8}}" var="team">
                      <g:link controller="team" action="show" params="${[ageBand:team.ageBand, teamName:team.name]}">${team}</g:link>
                  </g:each>
                  </li>

                  <g:each in="${(8..18)}" var="age">
                  <li>U${age} ::
                  <g:each in="${allTeams.grep{it.ageBand==age}.sort{a,b-> a.division.compareTo(b.division)}}" var="team">
                      <g:link
                              title="Manager: ${team.manager}"
                              controller="team" action="show"
                              params="${[ageBand:team.ageBand, teamName:team.name]}">${team.name}</g:link>
                  </g:each>
                  </li>
                  </g:each>
              </ul>
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

