<%@ page import="org.davisononline.footy.match.Fixture" %>

<div class="newsbox">
    <h2><g:message code="org.davisononline.footy.match.fixturecal.title" default="Fixture Calendar"/></h2>
    <table>
        <tbody>
        <g:each in="${fixtures}" var="fixture">
        <%-- set list icon to denote cup/friendly/league --%>
        <tr>
            <td>
                <strong><g:formatDate date="${fixture.dateTime}" format="dd'-'MMM HH:mm"/></strong>
            </td>
            <td>
            <footy:isManager team="${myteam}">
                <modalbox:createLink
                        controller="fixture"
                        action="editDialog"
                        id="${fixture.id}"
                        title="Add result/report"
                        width="450">
                    ${fixture.opposition}
                </modalbox:createLink>
                <g:if test="${!fixture.played && fixture.resources.size() == 0 && !fixture.referee}">
                <g:remoteLink
                        controller="fixture"
                        action="delete"
                        params="[fixtureId:fixture.id,teamId:myteam.id]"
                        update="fixtureList"
                        title="delete fixture"
                ><img src="${createLinkTo(dir:'images/skin', file:'database_delete.png', plugin:'footy-core')}" alt="del"/></g:remoteLink>
                </g:if>
                <g:else>
                <img src="${createLinkTo(dir:'images', file:'whistle_icon.png', plugin:'footy-match')}" alt="" title="Resources have been assigned to this fixture. Contact the fixture secretary to cancel" />
                </g:else>
            </footy:isManager>
            <footy:isNotManager team="${myteam}">
                ${fixture.opposition}
            </footy:isNotManager>
            </td>
            <td>
                ${fixture.homeGame ? 'H':'A'}&nbsp;${fixture.type == Fixture.CUP_GAME ? '(C)':''}${fixture.type == Fixture.FRIENDLY_GAME ? '(F)':''}
            </td>
        </tr>
        </g:each>
        <tr>
            <td colspan="3">
                <g:message code="org.davisononline.footy.match.fixturetypekey.label" default="(C) = Cup game, (F) = Friendly"/>
            </td>
        </tr>
        </tbody>
    </table>

    <ul>
    <footy:isManager team="${myteam}">
        <li class="inline">
            <img src="${createLinkTo(dir:'images/skin', file:'database_add.png', plugin:'footy-core')}" alt="add"/>
            <modalbox:createLink
                    controller="fixture"
                    action="createDialog"
                    id="${myteam.id}"
                    title="Create new ${myteam} Fixture"
                    width="450">
                <g:message code="org.davisononline.footy.match.label.create" default="New"/>
            </modalbox:createLink>
        </li>
    </footy:isManager>
    <g:if test="${totalFixtureCount > 0}">
        <li class="inline">
        <img src="${createLinkTo(dir:'images/skin', file:'database_table.png', plugin:'footy-core')}" alt=""/>
        <g:link controller="fixture" action="list" id="${myteam.id}">
            <g:message code="org.davisononline.footy.match.link.viewall" default="View All"/>
        </g:link>
        </li>
        <li class="inline">
        <img src="${createLinkTo(dir:'images', file:'cal.png', plugin:'footy-core')}" alt="ICS"/>
        <a href="${createLink(absolute:true, controller:'fixture', action:'calendar', id:myteam.id).replace('http','webcal').replace('https','webcal')}"
           title="Subscribe to ${myteam} calendar (Outlook/Thunderbird/iPhone etc.)">
            <g:message code="org.davisononline.footy.match.link.viewall" default="Subscribe"/>
        </a>
        </li>
    </g:if>
    <g:else>
        <li><g:message code="org.davisononline.footy.match.text.nofixtures" default="No fixtures have been created"/> </li>
    </g:else>
    </ul>
</div>
