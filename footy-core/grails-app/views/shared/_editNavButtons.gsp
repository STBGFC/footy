                <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
                <sec:ifAnyGranted roles="ROLE_CLUB_ADMIN">
                <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
                </sec:ifAnyGranted>