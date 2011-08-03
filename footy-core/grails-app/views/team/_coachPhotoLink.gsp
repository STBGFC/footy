                        <footy:isManager team="${teamInstance}">
                        <modalbox:createLink
                                controller="person"
                                action="photoUploadDialog"
                                id="${person.id}"
                                params="${[teamId:teamInstance.id]}"
                                title="Add or change ${person.givenName}`s photo"
                                width="400">
                            <img src="${createLinkTo(dir:'images', file:'camicon.png', plugin: 'footy-core')}" alt="Add or change ${person.givenName}'s photo"/>
                        </modalbox:createLink>
                        </footy:isManager>

