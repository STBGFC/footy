
<%@ page import="org.grails.paypal.Payment; org.davisononline.footy.tournament.Entry" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'entry.label', default: 'Entry')}" />
        <title><g:message code="default.entry.label" default="Entry Payment Status" /></title>
    </head>
    <body>
        <div class="list">
	        <p>
                <g:message code="org.davisononline.footy.tournament.views.entry.apply.entryList.para1"
                    default="Entries for this tournament are displayed in the table below with payment status.  You can delete an entry (and all teams on it from the tournament) if the payment has not been made."/>
	        </p>
	        <div class="nav">
	            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
                <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="['Tournament']" /></g:link></span>
	        </div>
            <table class="list">
                <thead>
                    <tr>
                        <th>${message(code: 'org.davisononline.footy.tournament.entry.contactName.label', default: 'Contact')}</th>
                        <th>${message(code: 'org.davisononline.footy.tournament.entry.teams.label', default: 'Teams')}</th>
                        <g:sortableColumn property="payment" title="${message(code: 'org.davisononline.footy.tournament.entry.payment.label', default: 'Payment Status')}" />
                    </tr>
                </thead>
                <tbody>
                <g:each in="${entries}" status="i" var="entry">
                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        <td>
                            ${entry.contact.encodeAsHTML()} (${entry.contact.phone1.encodeAsHTML()})
                        </td>
                        <td>
                            <g:each in="${entry.teams}" var="team">
                            ${team.club} ${team}<br/>
                            </g:each>
                        </td>
                        <td>
                            <g:if test="${entry.payment}">
                                <g:set var="cash" value="${entry.payment.status == Payment.COMPLETE && !entry.payment.paypalTransactionId}"/>
                                <g:link controller="invoice" action="show" id="${entry.payment?.transactionId}">
                                    <img title="Payment ${cash ? 'by Cash/Cheque': entry.payment.status}" alt="${entry.payment?.status?.toLowerCase()} (click to see invoice)"
                                         src="${resource(dir:'images',file:'payment-' + entry.payment?.status?.toLowerCase() + (cash ? 'b' : '') + '.png', plugin:'footy-core')}"/>
                                </g:link>
                            </g:if>
                            <g:if test="${entry.payment?.status != org.grails.paypal.Payment.COMPLETE}">
                            &nbsp;(<g:link action="paymentMade" controller="entry" id="${entry.id}" onclick="return confirm('${message(code: 'default.button.manualpayment.confirm.message', default: 'Are you sure you want to mark payment as received?')}');">mark payment received</g:link>)
                            &nbsp;(<g:link action="delete" controller="entry" id="${entry.id}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">delete entry</g:link>)
                            </g:if>
                        </td>
                    </tr>
                </g:each>
                </tbody>
            </table>
        </div>
    </body>
</html>
