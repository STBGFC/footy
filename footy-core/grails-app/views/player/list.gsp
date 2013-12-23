
<%@ page import="org.grails.paypal.PaymentItem; org.grails.paypal.Payment; org.davisononline.footy.core.Person" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'player.label', default: 'Player')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
        <export:resource/>
    </head>
    <body>
        <h1>
            <g:message code="org.davisononline.footy.core.playerlist.text" default="Players who play with the club"/>
        </h1>
        <div class="list">
            <div class="nav">
                <span class="menuButton"><a class="home" href="${createLink(uri: '/login/profile')}"><g:message code="default.home.label"/></a></span>
                <span class="menuButton"><g:link class="create" controller="registration"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
                <span class="menuButton"><g:link class="list" action="list" controller="person"><g:message code="default.list.label" args="['People']" /></g:link></span>
            </div>
            
                <table class="list">
                    <thead>
                        <tr>
                            <g:sortableColumn property="person.familyName" title="${message(code: 'person.name.label', default: 'Name')}" />
                            <g:sortableColumn property="dateOfBirth" title="${message(code: 'player.dateOfBirth.label', default: 'DoB')}" />
                            <th>${message(code: 'player.contactDetails.label', default: 'Contact')}</th>
                            <g:sortableColumn property="currentRegistration" title="${message(code: 'player.lastRegistrationDate.label', default: 'Registered Until')}" />
                            <g:sortableColumn property="leagueRegistrationNumber" title="${message(code: 'player.leagueRegistrationNumber.label', default: 'League Registration #')}" />
                            <g:sortableColumn property="team" title="${message(code: 'player.team.label', default: 'Team')}" />
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${playerInstanceList}" status="i" var="player">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                            <td><g:link action="edit" id="${player.id}">${player.person}</g:link></td>
                            <td><g:formatDate date="${player.dateOfBirth}" format="dd/MM/yyyy"/></td>
                            <td>
                                <g:link controller="person" action="edit" id="${player.guardian?.id}">${player.guardian}</g:link>
                                <br/>${player.guardian?.bestPhone().encodeAsHTML()}
                                <br/><a class="email" href="mailto:${player.guardian?.email}">${player.guardian?.email}</a>
                            </td>
                            <td>
                                <footy:registrationStatus player="${player}"/>
                                ${player.currentRegistration ?: "Not Registered"}
                            </td>
                            <td>${fieldValue(bean: player, field: "leagueRegistrationNumber")}</td>
                            <td>
                                <g:if test="${player?.team}">
                                <g:link action="edit" controller="team" id="${player.team.id}">${fieldValue(bean: player, field: "team")}</g:link>
                                </g:if>
                                <g:else>
                                    ${fieldValue(bean: player, field: "team")}
                                </g:else>
                            </td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${playerInstanceTotal}" />
            </div>
            <export:formats formats="['excel', 'pdf']" />
    </body>
</html>
