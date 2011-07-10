
<%@ page import="org.davisononline.footy.core.utils.PaymentUtils; org.grails.paypal.Payment; org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'org.grails.paypal.Payment.label', default: 'Payment Reconciliation')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="list">
            <p>
                <g:message code="org.davisononline.footy.core.payment.text" default="Payment reconciliations"/>
            </p>
            <div class="nav">
                <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
                <span class="menuButton"><g:link class="list" action="unpaid"><g:message code="org.davisononline.footy.core.unpaidinvoicelist.label" default="Unpaid invoices" /></g:link></span>
            </div>

                <table class="list">
                    <thead>
                        <tr>
                            <th>${message(code: 'org.grails.paypal.payment.transactionId.label', default: 'Club Transaction')}</th>
                            <th>${message(code: 'org.grails.paypal.payment.status.label', default: 'Status')}</th>
                            <th>${message(code: 'org.grails.paypal.payment.description.label', default: 'Description')}</th>
                            <th>${message(code: 'org.grails.paypal.payment.paypalTransactionId.label', default: 'PayPal Transaction')}</th>
                            <th>${message(code: 'org.grails.paypal.buyerinformation.receivernamelabel', default: 'PayPal Account Holder')}</th>
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
                                <footy:paymentStatus payment="${payment}"/>
                            </td>
                            <td>
                                <ul>
                                <g:each in="${payment.paymentItems}" var="item">
                                <li>${item.itemName}</li>
                                </g:each>
                                </ul>
                            </td>
                            <td>
                                <code>${payment?.paypalTransactionId}</code>
                            </td>
                            <td>
                                <g:if test="${!cash}">
                                ${payment?.buyerInformation?.receiverName ?: 'n/a'}<br/>
                                ${payment?.buyerInformation?.street ?: ''}, ${payment?.buyerInformation?.zip ?: ''}
                                </g:if>
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
