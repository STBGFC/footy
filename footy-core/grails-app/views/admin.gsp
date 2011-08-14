<%@ page import="org.davisononline.footy.core.Team" %>
<html>
  <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	    <title>${person ?: 'Staff'} Home Page</title>
  </head>

  <body>
      <div id="homemain">
          <h2>My Details</h2>
          <g:if test="${person}">
          <div style="float:left"><footy:personPhoto person="${person}"/></div>
          <div>
          <table>
              <tbody>
              <tr><td class="label">username</td><td class="value"><sec:username /></td></tr>
              <g:if test="${person}">
              <tr><td class="label">full name</td><td class="value">${person}</td></tr>
              <tr><td class="label">address</td><td class="value">${person.address}</td></tr>
              <tr><td class="label"></td><td class="value"><g:link controller="person" action="edit" id="${person.id}">edit details</g:link> </td></tr>
              </g:if>
              </tbody>
          </table>
          </div>

          <h2>My Teams</h2>
          <g:if test="${teams.size()>0}">
          <ul>
          <g:each in="${teams}" var="team">
              <li>
              <g:link
                  title="go to team page"
                  controller="team" action="show"
                  params="${[ageBand:team.ageBand, teamName:team.name]}">${team}</g:link>
              </li>
          </g:each>
          </ul>
          </g:if>
          <g:else>
          <g:message code="org.davisononline.footy.core.admin.noteams.text" default="No associated teams found"/>
          </g:else>
          </g:if>
          <g:else>
              logged in: <strong><sec:username /></strong> (no additional details found)
          </g:else>


      </div>
      <div id="newspanel">
          <div class="newsbox">
              <h2>Club Administration</h2>
              <ul>
                  <li>
                      <modalbox:createLink
                          controller="team"
                          action="messageDialog"
                          title="Send Email Message"
                          width="420">
                          Send Email Message(s)
                      </modalbox:createLink>
                  </li>
                  <sec:ifAnyGranted roles="ROLE_CLUB_ADMIN">
                  <li>
                      <g:link controller="person" action="list">Member List</g:link>
                  </li>
                  <li>
                      <g:link controller="player" action="list">Player List</g:link>
                  </li>
                  <li>
                      <g:link controller="team" action="list">Team List</g:link>
                  </li>
                  </sec:ifAnyGranted>
                  <sec:ifAnyGranted roles="ROLE_OFFICER">
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
                  </sec:ifAnyGranted>
              </ul>
          </div>

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

          <sec:ifAnyGranted roles="ROLE_EDITOR">
          <div class="newsbox">
              <h2>Content Administration</h2>
              <ul>
                  <li>
                      <g:link url="wcm-admin">All Content</g:link>
                  </li>
              </ul>
          </div>
          </sec:ifAnyGranted>
      </div>
  </body>
</html>
