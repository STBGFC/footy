
                        <tr class="prop">
                            <td  class="name">
                                <label for="givenName"><g:message code="org.davisononline.org.footy.core.contactGivenName.label" default="Given Name(s)" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: playerCommand, field: 'givenName', 'errors')}">
                                <g:textField name="givenName" value="${playerCommand?.givenName}" />
                                <g:render template="/fieldError" model="['instance':playerCommand,'field':'givenName']"/>
                            </td>
                        </tr>

                        <tr class="prop">
                            <td  class="name">
                                <label for="familyName"><g:message code="org.davisononline.org.footy.core.contactFamilyName.label" default="Family Name" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: playerCommand, field: 'familyName', 'errors')}">
                                <g:textField name="familyName" value="${playerCommand?.familyName}" />
                                <g:render template="/fieldError" model="['instance':playerCommand,'field':'familyName']"/>
                            </td>
                        </tr>

                        <tr class="prop">
                            <td  class="name">
                                <label for="knownAsName"><g:message code="org.davisononline.org.footy.core.contactKnownAsName.label" default="Known As" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: playerCommand, field: 'knownAsName', 'errors')}">
                                <g:textField name="knownAsName" value="${playerCommand?.knownAsName}" />
                                <g:render template="/fieldError" model="['instance':playerCommand,'field':'knownAsName']"/>
                            </td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="dateOfBirth"><g:message code="org.davisononline.org.footy.registration.playerDob.label" default="Date of Birth" /></label>
                            </td>
                            <td valign="top" class="value date">
                                <g:datePicker name="dateOfBirth" precision="day" years="${(new Date().year-19+1900)..(new Date().year-5+1900)}" value="${playerCommand?.dateOfBirth}"  />
                            </td>
                        </tr>