<%@ page import="org.grails.paypal.Payment" %>

                                    <ul class="clear">
                                    <g:each in="${payments.sort {a,b -> b.transactionId.compareTo(a.transactionId)}}" var="payment">
                                        <li>
                                        <g:set var="cash" value="${payment.status == Payment.COMPLETE && !payment.paypalTransactionId}"/>
                                        <img align="middle" title="Payment ${cash ? 'by Cash/Cheque': payment.status}" alt="${payment?.status?.toLowerCase()}"
                                            src="${resource(dir:'images',file:'payment-' + payment?.status?.toLowerCase() + (cash ? 'b' : '') + '.png', plugin:'footy-core')}"/>
                                        <g:if test="${payment != null}">
                                        <g:link controller="invoice" action="show" id="${payment?.transactionId}" params="[returnController:'registration']">
                                            ${payment?.transactionId}
                                        </g:link>
                                        </g:if>
                                        </li>
                                    </g:each>
                                    </ul>