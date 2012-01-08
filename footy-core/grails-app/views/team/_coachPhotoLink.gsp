                        <%-- can do this if a manager of the team passed in, or have ROLE_EDITOR --%>
                        <footy:isManager team="${teamInstance}">
                        <modalbox:createLink
                                controller="person"
                                action="photoUploadDialog"
                                id="${person.id}"
                                params="${[teamId:teamInstance.id]}"
                                title="Add or change ${person.givenName}`s photo"
                                width="400">
                            <r:img dir="images" file="camicon.png" plugin="footy-core"  alt="Add or change ${person.givenName}'s photo"/>
                        </modalbox:createLink>
                        </footy:isManager>
                        <footy:isNotManager team="${teamInstance}">
                        <sec:ifAnyGranted roles="ROLE_EDITOR">
                        <modalbox:createLink
                                controller="person"
                                action="photoUploadDialog"
                                id="${person.id}"
                                params="${[teamId:teamInstance.id]}"
                                title="Add or change ${person.givenName}`s photo"
                                width="400">
                            <r:img dir="images" file="camicon.png" plugin="footy-core"  alt="Add or change ${person.givenName}'s photo"/>
                        </modalbox:createLink>
                        </sec:ifAnyGranted>
                        </footy:isNotManager>

