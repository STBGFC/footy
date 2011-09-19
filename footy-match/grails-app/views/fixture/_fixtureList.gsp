<%@ page import="org.davisononline.footy.match.Fixture" %>

<div class="newsbox">
    <h2><g:message code="org.davisononline.footy.match.fixturecal.title" default="Fixture Calendar"/></h2>
    <ul>
        <g:each in="${fixtures}" var="fixture">
        <g:set var="tag" value="${fixture.opposition} (${fixture.homeGame ? 'H':'A'}) ${fixture.type == Fixture.CUP_GAME ? '(Cup)':''}"/>
        <%-- set list icon to denote cup/friendly/league --%>
        <li>
            <strong><g:formatDate date="${fixture.dateTime}" format="dd MMM"/>:</strong>
            <footy:isManager team="${myteam}">
                <modalbox:createLink
                        controller="fixture"
                        action="editDialog"
                        id="${fixture.id}"
                        title="Add result/report"
                        width="450">
                    ${tag}
                </modalbox:createLink>
                <g:if test="${!fixture.played}">
                <g:remoteLink
                        controller="fixture"
                        action="delete"
                        params="[fixtureId:fixture.id,teamId:myteam.id]"
                        update="fixtureList"
                        title="delete fixture"
                ><img src="${createLinkTo(dir:'images/skin', file:'database_delete.png', plugin:'footy-core')}" alt="del"/></g:remoteLink>
                </g:if>
            </footy:isManager>
            <footy:isNotManager team="${myteam}">
                ${tag}
            </footy:isNotManager>
        </li>
        </g:each>
    </ul>

    <ul>
    <footy:isManager team="${myteam}">
        <li class="inline">
            <img src="${createLinkTo(dir:'images/skin', file:'database_add.png', plugin:'footy-core')}" alt="add"/>
            <modalbox:createLink
                    controller="fixture"
                    action="createDialog"
                    id="${myteam.id}"
                    title="${myteam} Fixture"
                    width="450">
                <g:message code="org.davisononline.footy.match.label.create" default="New Fixture"/>
            </modalbox:createLink>
        </li>
    </footy:isManager>
        <li class="inline">
        <img src="${createLinkTo(dir:'images/skin', file:'database_table.png', plugin:'footy-core')}" alt=""/>
        <g:link controller="fixture" action="list" id="${myteam.id}">
            <g:message code="org.davisononline.footy.match.link.viewall" default="View All"/>
        </g:link>
        </li>
    </ul>
</div>
