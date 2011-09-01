                    <g:each in="${fixtures}" var="fixture">
                    <g:set var="home" value="${myteam == fixture.homeTeam}"/>
                    <g:set var="oppo" value="${home ? fixture.awayTeam : fixture.homeTeam}"/>
                    <li>
                        <g:formatDate date="${fixture.dateTime}" format="dd/MM HH:mm"/>: ${oppo.club} ${oppo.name} (${home ? 'H' : 'A'})
                    </li>
                    </g:each>