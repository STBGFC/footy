
                                    <ul class="nice-list">
                                    <g:each in="${person.qualifications}" var="qual">
                                    <g:set var="now" value="${new Date()}"/>
                                    <g:set var="qualClass" value="${(qual.expiresOn && qual.expiresOn < now) ? 'qualExpired' : 'qualInDate'}"/>
                                        <li>
                                            <span class="${qualClass}">${qual}</span>
                                            <sec:ifAnyGranted roles="ROLE_CLUB_ADMIN">
                                            <g:remoteLink
                                                    action="delQualification"
                                                    params="[personId:person.id,qualificationId:qual.id]"
                                                    update="qualifications"
                                                    title="delete"
                                            ><img src="${createLinkTo(dir:'images', file:'delete.png', plugin: 'footy-core')}" alt="del"/></g:remoteLink>
                                            </sec:ifAnyGranted>
                                        </li>
                                    </g:each>
                                    <g:if test="${person.qualifications.size()==0}">
                                        <li>No qualifications</li>
                                    </g:if>
                                    </ul>
