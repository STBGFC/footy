
<%@ page import="org.davisononline.footy.core.Team; org.davisononline.footy.tournament.Tournament" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'tournament.label', default: 'Tournament')}" />
        <title>${tournamentInstance.name}</title>
        <export:resource />
    </head>
    <body>
        <div class="list">
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        
        <g:if test="${teamList.size() > 0}">
        <p>
            The following teams are entered in this competition so far (note that not all money may have been received,
            check <g:link controller="tournament" action="entryList" id="${tournamentInstance.id}">here</g:link> to see status of payments)
        </p>
        <g:each in="${Team.constraints.ageBand.inList}" var="age">
            <g:render template="teamTables" model="['teams':teamList.grep{it.ageBand == age && !it.girlsTeam}, 'age': age]" />
        </g:each>
        <g:each in="${Team.constraints.ageBand.inList}" var="age">
            <g:render template="teamTables" model="['teams':teamList.grep{it.ageBand == age && it.girlsTeam}, 'age': age]" />
        </g:each>
        <p>
            You can export the data above to Excel or PDF using the buttons below
        </p>
        <export:formats params="[id:tournamentInstance.id]" formats="['excel', 'pdf']" />
        </div>
        </g:if>
        
        <g:else>
        <p>
            No teams have been entered for this tournament yet.
        </p>        
        </g:else>
    </body>
</html>
