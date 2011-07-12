
                                    <ul>
                                    <g:each in="${person.qualifications}" var="qual">
                                    <g:set var="now" value="${new Date()}"/>
                                    <g:set var="qualClass" value="${(qual.expiresOn && qual.expiresOn < now) ? 'qualExpired' : 'qualInDate'}"/>
                                        <li>
                                            <span class="${qualClass}">${qual}</span>
                                            <g:remoteLink
                                                    action="delQualification"
                                                    params="[personId:person.id,qualificationId:qual.id]"
                                                    update="qualifications"
                                                    title="delete"
                                            ><img src="${createLinkTo(dir:'images/skin', file:'database_delete.png')}" alt="del"/></g:remoteLink>
                                        </li>
                                    </g:each>
                                    <g:if test="${person.qualifications.size()==0}">
                                        <li>No qualifications</li>
                                    </g:if>
                                    </ul>
