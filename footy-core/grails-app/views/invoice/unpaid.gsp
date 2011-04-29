
<%@ page import="org.davisononline.footy.core.utils.PaymentUtils; org.grails.paypal.Payment; org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'org.grails.paypal.Payment.label', default: 'Unpaid Invoices')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="list">
            <p>
                <g:message code="org.davisononline.footy.core.payment.text" default="Payment reconciliations"/>
            </p>
            <div class="nav">
                <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
                <span class="menuButton"><g:link class="list" action="list"><g:message code="org.davisononline.footy.core.invoicelist.label" default="PayPal Reconciliation" /></g:link></span>
            </div>

                <table class="list">
                    <thead>
                        <tr>
                            <th>${message(code: 'org.grails.paypal.payment.transactionId.label', default: 'Invoice')}</th>
                            <th>${message(code: 'org.grails.paypal.payment.status.label', default: 'Status')}</th>
                            <th>${message(code: 'org.grails.paypal.payment.description.label', default: 'Items')}</th>
                            <th>${message(code: 'org.grails.paypal.payment.transactionId.label', default: 'Amount')}</th>
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${paymentList}" status="i" var="payment">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                            <td>
                                <g:link action="show" id="${payment.transactionId}">${payment?.transactionId}</g:link>
                            </td>
                            <td>
                                <img title="${payment?.status}" alt="${payment?.status?.toLowerCase()}" src="${resource(dir:'images',file:'payment-' + payment?.status?.toLowerCase() + '.png', plugin:'footy-core')}"/>
                            </td>
                            <td>
                                <ul>
                                <g:each in="${payment.paymentItems}" var="item">
                                <li>${item.itemName}</li>
                                </g:each>
                                </ul>
                            </td>
                            <td>
                                ${payment ? formatNumber(number:PaymentUtils.calculateTotal(payment), type:'currency', currencyCode:payment.currency) : 'n/a'}
                            </td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${paymentTotal}" />
            </div>
    </body>
</html>
