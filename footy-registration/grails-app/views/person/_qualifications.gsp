            <ul id="coachDetails${p.id}" class="clearList">
                <g:each in="${p?.coachingQualifications}" var="cq">
                <li><g:formatDate format="yyyy-MM-dd" date="${cq.dateAttained}"/>: ${cq.type}</li>
                </g:each>
            </ul>