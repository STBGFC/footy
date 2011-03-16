<%@ page import="org.davisononline.footy.core.*" %>
                        <tr class="prop">
                            <td  class="name">
                                <label for="person.givenName"><g:message code="org.davisononline.org.footy.core.contactGivenName.label" default="Given Name(s)" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: playerInstance.person, field: 'givenName', 'errors')}">
                                <g:textField name="person.givenName" value="${playerInstance?.person?.givenName}" />
                                <g:render template="/fieldError" model="['instance':playerInstance.person,'field':'givenName']"/>
                            </td>
                        </tr>
                        <tr class="prop">
                            <td  class="name">
                                <label for="person.familyName"><g:message code="org.davisononline.org.footy.core.contactFamilyName.label" default="Family Name" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: playerInstance.person, field: 'familyName', 'errors')}">
                                <g:textField name="person.familyName" value="${playerInstance?.person?.familyName}" />
                                <g:render template="/fieldError" model="['instance':playerInstance.person,'field':'familyName']"/>
                            </td>
                        </tr>

                        <tr class="prop">
                            <td  class="name">
                                <label for="person.knownAsName"><g:message code="org.davisononline.org.footy.core.contactKnownAsName.label" default="Known As" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: playerInstance.person, field: 'knownAsName', 'errors')}">
                                <g:textField name="person.knownAsName" value="${playerInstance?.person?.knownAsName}" />
                                <g:render template="/fieldError" model="['instance':playerInstance.person,'field':'knownAsName']"/>
                            </td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="dateOfBirth"><g:message code="org.davisononline.org.footy.registration.playerDob.label" default="Date of Birth" /></label>
                            </td>
                            <td valign="top" class="value date">
                                <g:datePicker name="dateOfBirth" precision="day" years="${(new Date().year-19+1900)..(new Date().year-5+1900)}" value="${playerInstance?.dateOfBirth}"  />
                            </td>
                        </tr>

                        <tr class="prop">
                            <td  class="name">
                                <label for="guardian.id"><g:message code="org.davisononline.org.footy.core.playerGuardian.label" default="Parent/Guardian" /></label>
                            </td>
                            <td  class="value">
                                <g:select name="guardian.id" from="${Person.findAllEligibleParent()}" noSelection="[null:'-- Not listed or not applicable --']" optionKey="id" value="${playerInstance?.guardian?.id}"/>
                                <g:render template="/fieldError" model="['instance':playerInstance,'field':'guardian']"/>
                            </td>
                        </tr>

                        <tr class="prop">
                            <td  class="name">
                                <label for="sibling.id"><g:message code="org.davisononline.org.footy.core.playerGuardian.label" default="Sibling (for discount)" /></label>
                            </td>
                            <td  class="value">
                                <g:select name="sibling.id" from="${Player.list()}" noSelection="[null:'-- Not applicable --']" optionKey="id" value="${playerInstance?.sibling?.id}"/>
                                <g:render template="/fieldError" model="['instance':playerInstance,'field':'sibling']"/>
                            </td>
                        </tr>