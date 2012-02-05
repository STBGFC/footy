<%@ page import="org.davisononline.footy.core.Team" %>
<html>
  <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	    <title>${person ?: 'Staff'}'s Profile Page</title>
  </head>

  <body>
      <div id="homemain">
          
          <h2>My Details</h2>
          <g:if test="${person}">
          <div style="float:left">
                <modalbox:createLink
                        controller="person"
                        action="photoUploadDialog"
                        id="${person.id}"
                        title="Add or change your photo"
                        width="400">
                    <footy:personPhoto person="${person}"/>
                </modalbox:createLink>
              </div>
          <div style="float:left">
              <table>
                  <tbody>
                  <tr><td class="label">username</td><td class="value"><sec:username /></td></tr>
                  <g:if test="${person}">
                  <tr><td class="label">full name</td><td class="value">${person}</td></tr>
                  <tr><td class="label">email</td><td class="value">${person.email}</td></tr>
                  <tr><td class="label">mobile</td><td class="value">${person.phone1}</td></tr>
                  <tr><td class="label">other phone</td><td class="value">${person.phone2}</td></tr>
                  <tr><td class="label">address</td><td class="value">${person.address}</td></tr>
                  <tr><td>qualifications</td><td><g:render template="/person/qualificationsList" model="[person:person]" plugin="footy-core"/></td></tr>
                  <tr><td class="label"></td><td class="value"><g:link controller="person" action="edit" id="${person.id}">edit details</g:link> </td></tr>
                  </g:if>
                  </tbody>
              </table>
          </div>
          </g:if>
          <g:else>
              logged in: <strong><sec:username /></strong> (no additional details found)
          </g:else>


      </div>
      <div id="newspanel">
          <g:if test="${teams.size()>0}">
          <div class="newsbox">
              <h2><g:message code="org.davisononline.footy.core.myteams.label" default="My Teams" /></h2>
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
          </div>
          </g:if>

          <div class="newsbox">
              <h2><g:message code="org.davisononline.footy.core.links.label" default="Pages and Links" /></h2>
              <ul>
                  <%-- TODO: stuff below implies knowledge of other optional plugins.. needs making safe --%>
                  <li>
                      <g:link controller="resource" action="summary">
                          <g:message code="org.davisononline.footy.match.pitches.label" default="Pitch Allocations" />
                      </g:link>
                  </li>
                  <li>
                      <g:link url="../content/staff/">
                          <g:message
                                  code="org.davisononline.footy.core.staffcontent.label"
                                  default="Managers Pages" />
                      </g:link>
                  </li>
              </ul>
          </div>
          
          <div class="newsbox">
              <h2><g:message code="org.davisononline.footy.core.administration.label" default="Administration" /></h2>
              <ul>
                  <li>
                      <g:link controller="login" action="changePassword">Change Password</g:link>
                  </li>
                  <li>
                      <modalbox:createLink
                          controller="team"
                          action="messageDialog"
                          title="Send Email Message"
                          width="420">
                          Send Email Message(s)
                      </modalbox:createLink>
                  </li>
              </ul>
              <sec:ifAnyGranted roles="ROLE_CLUB_ADMIN">
              <ul>
                  <li>
                      <g:link controller="person" action="list">Member List</g:link>
                  </li>
                  <li>
                      <g:link controller="person" action="qualifications">Qualifications expiring soon</g:link>
                  </li>
                  <li>
                      <g:link controller="player" action="list">Player List</g:link>
                  </li>
                  <li>
                      <g:link controller="team" action="list">Team List</g:link>
                  </li>
                  <li>
                      <g:link controller="league" action="list">Leagues/Divisions List</g:link>
                  </li>
                  <li>
                      <g:link controller="sponsor" action="list">Sponsor List</g:link>
                  </li>
                  <li>
                      <g:link controller="club" action="addresscards">Download all parent contact details</g:link>
                  </li>
              </ul>
              </sec:ifAnyGranted>
              <sec:ifAnyGranted roles="ROLE_OFFICER">
              <ul>
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
              </sec:ifAnyGranted>
              <%-- TODO: stuff below implies knowledge of other optional plugins.. needs making safe --%>
              <ul>
                  <sec:ifAnyGranted roles="ROLE_EDITOR">
                  <li>
                      <g:link url="../wcm-admin">
                          <g:message
                                  code="org.davisononline.footy.core.contentadministration.label"
                                  default="Content Administration" />
                      </g:link>
                  </li>
                  </sec:ifAnyGranted>
                  <sec:ifAnyGranted roles="ROLE_TOURNAMENT_ADMIN">
                  <li>
                      <g:link controller="tournament" action="list">
                          <g:message
                                  code="org.davisononline.footy.core.tournamentadmin.label"
                                  default="Tournament Administration" />
                      </g:link>
                  </li>
                  </sec:ifAnyGranted>
                  <sec:ifAnyGranted roles="ROLE_FIXTURE_ADMIN">
                  <li>
                      <g:link controller="resource" action="index">
                          <g:message
                                  code="org.davisononline.footy.match.fixtureadmin.label"
                                  default="Fixture Resource Allocations" />
                      </g:link>
                  </li>
                  <li>
                      <g:link controller="resource" action="reports">
                          <g:message
                                  code="org.davisononline.footy.match.refreportsummary.label"
                                  default="Referee Report Summary" />
                      </g:link>
                  </li>
                  </sec:ifAnyGranted>
              </ul>
          </div>
      </div>
  </body>
</html>
