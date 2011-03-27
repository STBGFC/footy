
<%@ page import="org.davisononline.footy.core.RegistrationTier" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'registrationTier.label', default: 'RegistrationTier')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="list">
            <p>
                <g:message code="org.davisononoline.footy.core.registrationtier.text" default="Current registration tiers"/>
            </p>
            <div class="nav">
                <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
                <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </div>

            <table class="list">
                <thead>
                    <tr>
                        <g:sortableColumn property="name" title="${message(code: 'registrationTier.name.label', default: 'Name')}" />
                        <g:sortableColumn property="amount" title="${message(code: 'registrationTier.amount.label', default: 'Amount')}" />
                        <g:sortableColumn property="repeatDiscount" title="${message(code: 'registrationTier.repeatDiscount.label', default: 'Repeat Discount')}" />
                        <g:sortableColumn property="siblingDiscount" title="${message(code: 'registrationTier.siblingDiscount.label', default: 'Sibling Discount')}" />
                    </tr>
                </thead>
                <tbody>
                <g:each in="${registrationTierInstanceList}" status="i" var="registrationTierInstance">
                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        <td><g:link action="edit" id="${registrationTierInstance.id}">${fieldValue(bean: registrationTierInstance, field: "name")}</g:link></td>
                        <td><g:formatNumber number="${registrationTierInstance.amount}" type="currency" currencyCode="${currency}" /></td>
                        <td><g:formatNumber number="${registrationTierInstance.repeatDiscount}" type="currency" currencyCode="${currency}" /></td>
                        <td><g:formatNumber number="${registrationTierInstance.siblingDiscount}" type="currency" currencyCode="${currency}" /></td>
                    </tr>
                </g:each>
                </tbody>
            </table>
            <div class="paginateButtons">
                <g:paginate total="${registrationTierInstanceTotal}" />
            </div>
        </div>
    </body>
</html>