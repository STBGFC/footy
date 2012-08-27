<%@ page import="org.grails.paypal.Payment" %>

                                    <ul class="nice-list">
                                    <g:each in="${payments.sort {a,b -> b.transactionId.compareTo(a.transactionId)}}" var="payment">
                                        <li>
                                        <footy:paymentStatus payment="${payment}"/>
                                        <g:if test="${payment != null}">
                                        <g:link controller="invoice" action="show" id="${payment?.transactionId}" params="[returnController:'registration']">
                                            ${payment?.transactionId}
                                        </g:link>
                                        </g:if>
                                        </li>
                                    </g:each>
                                    </ul>