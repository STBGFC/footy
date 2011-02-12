
<%@ page import="org.davisononline.footy.tournament.Tournament" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'tournament.label', default: 'Tournament')}" />
        <title>${tournamentInstance.name}</title>
    </head>
    <body>
        <div class="list">
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        
        <p>
            The following teams are entered in this competition so far (note that not all money may have been received,
            check <g:link controller="tournament" action="entryList" id="${tournamentInstance.id}">here</g:link> to see status of payments)
        </p>
        <ul>
        <g:each in="${tournamentInstance.teamsEntered()}" var="team">
            <li>${team}</li>
        </g:each>
        </ul>
        </div>
    </body>
</html>
