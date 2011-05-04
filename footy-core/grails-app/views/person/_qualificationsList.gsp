
                                    <ul class="clear">
                                    <g:each in="${person.qualifications}" var="qual">
                                    <g:set var="now" value="${new Date()}"/>
                                    <g:set var="qualClass" value="${(qual.expiresOn && qual.expiresOn < now) ? 'qualExpired' : 'qualInDate'}"/>
                                        <li>
                                            <span class="${qualClass}">${qual}</span>
                                            <%-- can't get g:remoteLink to work as required here... --%>
                                            <a href="/stbgfc/person/delQualification/${person.id}/${qual.id}"
                                                onclick="new Ajax.Updater('qualifications','/stbgfc/person/delQualification/${person.id}/${qual.id}',{asynchronous:true,evalScripts:true});return false;">(del)</a>
                                        </li>
                                    </g:each>
                                    </ul>
