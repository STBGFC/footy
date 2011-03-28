
<%@ page import="org.grails.paypal.Payment; org.davisononline.footy.core.Person" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'player.label', default: 'Player')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="list">
            <p>
                <g:message code="org.davisononline.footy.core.playerlist.text" default="Players who play with the club"/>
            </p>
            <div class="nav">
                <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
                <span class="menuButton"><g:link class="create" controller="registration"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
                <span class="menuButton"><g:link class="list" action="list" controller="person"><g:message code="default.list.label" args="['People']" /></g:link></span>
            </div>
            
                <table class="list">
                    <thead>
                        <tr>
                            <g:sortableColumn property="person.familyName" title="${message(code: 'person.name.label', default: 'Name')}" />
                            <g:sortableColumn property="dateOfBirth" title="${message(code: 'player.dateOfBirth.label', default: 'DoB')}" />
                            <th>${message(code: 'player.contactDetails.label', default: 'Contact')}</th>
                            <g:sortableColumn property="lastRegistrationDate" title="${message(code: 'player.lastRegistrationDate.label', default: 'Last Registration')}" />
                            <g:sortableColumn property="leagueRegistrationNumber" title="${message(code: 'player.leagueRegistrationNumber.label', default: 'League Registration #')}" />
                            <th>Team</th>
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${playerInstanceList}" status="i" var="player">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                            <td><g:link action="edit" id="${player.id}">${player.person}</g:link></td>
                            <td><g:formatDate date="${player.dateOfBirth}" format="dd/MM/yyyy"/></td>
                            <td>
                                ${player.guardian}<br/>${player.guardian?.phone1}<br/><a href="mailto:${player.guardian?.email}">${player.guardian?.email}</a>
                            </td>
                            <td>
                                <g:formatDate date="${player.lastRegistrationDate}" format="dd/MM/yyyy"/>
                                <%-- TODO: ensure most recent only is displayed --%>
                                <g:set value="${Payment.findByBuyerId(player.id)}" var="payment"/>
                                <g:if test="${payment != null}">
                                <g:link controller="invoice" action="show" id="${payment?.transactionId}" params="[returnController:'registration']">
                                    <img title="Payment ${payment?.status}" alt="${payment?.status?.toLowerCase()} (click to see invoice)" src="${resource(dir:'images',file:'payment-' + payment?.status?.toLowerCase() + '.png', plugin:'footy-core')}"/>
                                </g:link>
                                <g:if test="${payment?.status != org.grails.paypal.Payment.COMPLETE}">
                                <br/><g:link action="paymentMade" controller="player" id="${payment.transactionId}" onclick="return confirm('${message(code: 'default.button.manualpayment.confirm.message', default: 'Are you sure you want to mark payment as received?')}');">mark payment received</g:link>
                                <br/><g:link action="delete" controller="player" id="${player.id}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">delete player</g:link>
                                </g:if>
                                </g:if>
                            </td>
                            <td>${fieldValue(bean: player, field: "leagueRegistrationNumber")}</td>
                            <td>${fieldValue(bean: player, field: "team")}</td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${playerInstanceTotal}" />
            </div>
    </body>
</html>
