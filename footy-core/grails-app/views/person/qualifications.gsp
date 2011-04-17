
<%@ page import="org.davisononline.footy.core.Person" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title><g:message code="org.davisononline.footy.core.person.qualifications.title" default="Qualification List" /></title>
    </head>
    <body>
        <div class="list">
            <p>
                Qualifications in order of most urgent for renewal
            </p>
            <div class="nav">
                <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
                <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="['Person']" /></g:link></span>
            </div>
            
                <table class="list">
                    <thead>
                        <tr>
                            <th>${message(code: 'person.name.label', default: 'Name')}</th>
                            <th>${message(code: 'qualification.type.label', default: 'Qualification')}</th>
                            <th>${message(code: 'qualification.expireOn.label', default: 'Expires')}</th>
                        </tr>
                    </thead>
                    <tbody>
                    <g:set var="now" value="${new Date()}"/>
                    <g:each in="${qualifications}" status="i" var="qual">
                        <g:set var="dateMarker" value="${qual.expiresOn < now ? 'qualExpired' : (qual.expiresOn < (now + 90) ? 'qualAlmostExpired' : 'qualIndate')}"/>
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                            <td><g:link action="edit" id="${qual.person.id}">${qual.person}</g:link></td>
                            <td><span class="${dateMarker}">${qual.type.name}</span></td>
                            <td>
                                <g:if test="${dateMarker != 'qualIndate'}">
                                <img
                                    src="${resource(dir:'images',file:dateMarker + '.png', plugin:'footy-core')}"
                                    title="${dateMarker == 'qualExpired' ? 'EXPIRED!' : 'Close to expiry'}"
                                    align="middle"
                                    alt=""
                                />
                                </g:if>
                                <span class="${dateMarker}"><g:formatDate date="${qual.expiresOn}" format="dd/MM/yyyy"/></span>
                            </td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${qualificationsTotal}" />
            </div>
    </body>
</html>
