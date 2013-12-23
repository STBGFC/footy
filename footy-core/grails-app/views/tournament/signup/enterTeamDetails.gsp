<%@ page import="org.davisononline.footy.tournament.Competition; org.davisononline.footy.tournament.Entry" %>
<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title><g:message code="org.davisononline.footy.tournament.views.entry.apply.teamdetails.title" default="Enter Team Details" /></title>
    </head>
    <body>
        <h1><g:message code="org.davisononline.footy.tournament.views.entry.apply.teamdetails.title" default="Enter Team Details" /></h1>
        <div class="dialog">
            <g:form action="signup">
                <p>
                    Enter your team name, league &amp; relative strength within the league you play in (or make an educated guess).  The
                    strength indicator is just to help us with seeding for teams for leagues we are not familiar with.
                </p>
                <table>

                    <tbody>

                        <tr class="prop">
                            <td  class="name">
                                <label for="clubAndTeam"><g:message code="org.davisononline.footy.tournament.views.entry.apply.clubAndTeam.label"
                                                                    default="Full Team Name i.e 'STBGFC U11 Rovers'" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: entry, field: 'clubAndTeam', 'errors')}">
                                <g:textField name="clubAndTeam" value="${entry?.clubAndTeam}" />
                                <g:render template="/shared/fieldError" model="['instance':entry,'field':'clubAndTeam']" plugin="footy-core"/>
                            </td>
                        </tr>

                        <tr class="prop">
                            <td  class="name">
                                <label for="league"><g:message code="org.davisononline.footy.tournament.views.entry.apply.league.label"
                                                                    default="League Name (if any)" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: entry, field: 'league', 'errors')}">
                                <g:textField name="league" value="${entry?.league}" />
                                <g:render template="/shared/fieldError" model="['instance':entry,'field':'league']" plugin="footy-core"/>
                            </td>
                        </tr>

                        <tr class="prop">
                            <td  class="name">
                                <label for="strength"><g:message code="org.davisononline.footy.tournament.views.entry.apply.strength.label"
                                                                    default="Team Strength (based on division or an educated guess)" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: entry, field: 'strength', 'errors')}">
                                <g:select name="strength" value="${entry?.strength}" from="${Entry.STRENGTH_LIST}" />
                                <g:render template="/shared/fieldError" model="['instance':entry,'field':'strength']" plugin="footy-core"/>
                            </td>
                        </tr>

                        <tr class="prop">
                            <td  class="name">
                                <label for="competition"><g:message code="org.davisononline.footy.tournament.views.entry.apply.competition.label"
                                                                    default="Competition/Section to enter" /></label>
                            </td>
                            <td  class="value">
                                <g:select name="competition" value="${competition}" from="${tournament.competitions.findAll{it.open}.sort{it.name}}" optionKey="id" />
                            </td>
                        </tr>

                        <g:if test="${tournament.competitions.findAll{!it.open}.size()>0}">
                        <tr class="prop">
                            <td  class="name">
                                <g:message code="org.davisononline.footy.tournament.views.entry.apply.closedcompetition.label"
                                                                    default="Sorry, but these competitions are CLOSED to new entries.." />
                            </td>
                            <td  class="value">
                                <ul>
                                    <g:each var="comp" in="${tournament.competitions.findAll{!it.open}.sort{it.name}}"><li>${comp}</li></g:each>
                                </ul>
                            </td>
                        </tr>
                        </g:if>

                    </tbody>
                </table>
                <div class="buttons">
                    <span class="button"><g:submitButton name="submit" class="save" value="${message(code: 'team.button.submit.label', default: 'Submit Team')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
