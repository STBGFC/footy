                        <tr class="prop">
                            <td  class="name">
                                <label for="givenName"><g:message code="org.davisononline.org.footy.core.contactGivenName.label" default="Given Name" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: personCommand, field: 'givenName', 'errors')}">
                                <g:textField name="givenName" value="${personCommand?.givenName}" />
                                <g:render template="/shared/fieldError" model="['instance':personCommand,'field':'givenName']" plugin="footy-core"/>
                            </td>
                        </tr>

                        <tr class="prop">
                            <td  class="name">
                                <label for="familyName"><g:message code="org.davisononline.org.footy.core.contactFamilyName.label" default="Family Name" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: personCommand, field: 'familyName', 'errors')}">
                                <g:textField name="familyName" value="${personCommand?.familyName}" />
                                <g:render template="/shared/fieldError" model="['instance':personCommand,'field':'familyName']" plugin="footy-core"/>
                            </td>
                        </tr>

                        <tr class="prop">
                            <td  class="name">
                                <label for=phone1><g:message code="org.davisononline.org.footy.core.phone1.label" default="Contact Tel." /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: personCommand, field: 'phone1', 'errors')}">
                                <g:textField name="phone1" value="${personCommand?.phone1}" />
                                <g:render template="/shared/fieldError" model="['instance':personCommand,'field':'phone1']" plugin="footy-core"/>
                            </td>
                        </tr>

                        <tr class="prop">
                            <td  class="name">
                                <label for="email"><g:message code="org.davisononline.org.footy.core.email.label" default="Email" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: personCommand, field: 'email', 'errors')}">
                                <g:textField name="email" value="${personCommand?.email}" />
                                <g:render template="/shared/fieldError" model="['instance':personCommand,'field':'email']" plugin="footy-core"/>
                            </td>
                        </tr>

                        <tr class="prop">
                            <td  class="name">
                                <label for="occupation"><g:message code="org.davisononline.org.footy.core.occupation.label" default="Occupation" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: personCommand, field: 'occupation', 'errors')}">
                                <g:textField name="occupation" value="${personCommand?.occupation}" />
                                <g:render template="/shared/fieldError" model="['instance':personCommand,'field':'occupation']" plugin="footy-core"/>
                            </td>
                        </tr>

                        <tr class="prop">
                            <td  class="name">
                                <label for="address.address"><g:message code="org.davisononline.org.footy.core.address.label" default="Home Address" /></label>
                            </td>
                            <td  class="value">
                                House number or name:<br/>
                                <g:textField name="address.house" value="${personCommand?.address?.house}" />
                                <g:render template="/shared/fieldError" model="['instance':personCommand.address,'field':'house']" plugin="footy-core"/>
                                <br/>Street:<br/>
                                <g:textField name="address.address" value="${personCommand?.address?.address}" />
                                <g:render template="/shared/fieldError" model="['instance':personCommand.address,'field':'address']" plugin="footy-core"/>
                                <br/>Town:<br/>
                                <g:textField name="address.town" value="${personCommand?.address?.town}" />
                                <g:render template="/shared/fieldError" model="['instance':personCommand.address,'field':'town']" plugin="footy-core"/>
                                <br/>Post Code:<br/>
                                <g:textField name="address.postCode" value="${personCommand?.address?.postCode}" />
                                <g:render template="/shared/fieldError" model="['instance':personCommand.address,'field':'postCode']" plugin="footy-core"/>
                            </td>
                        </tr>

                        <tr class="prop">
                            <td  class="name">
                                <label for="notes"><g:message code="org.davisononline.footy.core.notes.label" default="Notes" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: personCommand, field: 'notes', 'errors')}">
                                <g:textArea name="notes" value="${personCommand?.notes}" rows="4" cols="30"/>
                                <g:render template="/shared/fieldError" model="['instance':personCommand,'field':'notes']" plugin="footy-core"/>
                            </td>
                        </tr>