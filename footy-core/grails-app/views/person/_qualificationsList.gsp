
                                    <ul class="clear">
                                    <g:each in="${person.qualifications}" var="qual">
                                    <g:set var="now" value="${new Date()}"/>
                                    <g:set var="qualClass" value="${(qual.expiresOn && qual.expiresOn < now) ? 'qualExpired' : 'qualInDate'}"/>
                                        <li>
                                            <span class="${qualClass}">${qual}</span>
                                            <g:remoteLink
                                                    action="delQualification"
                                                    params="[personId:person.id,qualificationId:qual.id]"
                                                    update="qualifications"
                                            >(del)</g:remoteLink>
                                        </li>
                                    </g:each>
                                    </ul>
