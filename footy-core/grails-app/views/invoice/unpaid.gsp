
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
                <g:message code="org.davisononline.footy.core.nopayment.text" default="Payment reconciliations"/>
            </p>
            <div class="nav">
                <span class="menuButton"><a class="home" href="${createLink(uri: '/login/profile')}"><g:message code="default.home.label"/></a></span>
                <span class="menuButton"><g:link class="list" action="list"><g:message code="org.davisononline.footy.core.invoicelist.label" default="Payment Reconciliation" /></g:link></span>
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
                    <g:set var="totalAmount" value="${PaymentUtils.calculateTotal(payment)}"/>
                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        <td>
                            <g:link action="show" id="${payment.transactionId}">${payment?.transactionId}</g:link>
                        </td>
                        <td>
                            <footy:paymentStatus payment="${payment}"/>
                            <modalbox:createLink
                                    controller="invoice"
                                    action="paymentDialog"
                                    id="${payment.transactionId}"
                                    params="${[totalAmount:totalAmount]}"
                                    title="Invoice ${payment.transactionId}"
                                    width="350">
                                <g:message code="org.grails.paypal.payment.markreceived" default="mark received"/>
                            </modalbox:createLink>
                            <sec:ifAllGranted roles="ROLE_OFFICER">
                                :: <g:link action="delete" id="${payment.transactionId}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" >delete</g:link>
                            </sec:ifAllGranted>
                        </td>
                        <td>
                            <ul>
                            <g:each in="${payment.paymentItems}" var="item">
                            <li>${item.itemName}</li>
                            </g:each>
                            </ul>
                        </td>
                        <td>
                            ${payment ? formatNumber(number:totalAmount, type:'currency', currencyCode:payment.currency) : 'n/a'}
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
