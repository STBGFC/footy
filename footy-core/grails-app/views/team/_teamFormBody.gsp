<%@ page import="org.davisononline.footy.core.*" %>
                            <tr class="prop">
                                <td  class="name">
                                    <label for="club.id"><g:message code="entry.club.label" default="Club" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: teamCommand, field: 'club', 'errors')}">
                                    <strong>${clubInstance.name}</strong>
                                    <g:hiddenField name="club.id" value="${clubInstance.id}"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="ageBand"><g:message code="entry.ageBand.label" default="Age Group" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: teamCommand, field: 'ageBand', 'errors')}">
                                    Under: <g:select class="short" name="ageBand" from="${Team.constraints.ageBand.inList}" value="${teamCommand?.ageBand}" valueMessagePrefix="entry.ageBand"  />
                                    <br/>
                                    <g:checkBox name="girlsTeam" value="${teamCommand?.girlsTeam}" /> (Girls)
                                    <g:checkBox name="vetsTeam" value="${teamCommand?.vetsTeam}" /> (Vets/Parents)
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="name"><g:message code="entry.teamName.label" default="Team Name" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: teamCommand, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${teamCommand?.name}" />
                                    <g:render template="/shared/fieldError" model="['instance':teamCommand,'field':'name']" plugin="footy-core"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="league.id"><g:message code="entry.league.label" default="League" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: teamCommand, field: 'league.id', 'errors')}">
                                    <g:select name="league.id" from="${League.list()}" value="${teamCommand?.league}" optionValue="name" optionKey="id" valueMessagePrefix="entry.league"  />
                                    <g:render template="/shared/fieldError" model="['instance':teamCommand,'field':'league']" plugin="footy-core"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="division"><g:message code="entry.division.label" default="Division" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: teamCommand, field: 'division', 'errors')}">
                                    <g:textField name="division" value="${teamCommand?.division}" />
                                    <g:render template="/shared/fieldError" model="['instance':teamCommand,'field':'division']" plugin="footy-core"/>
                                </td>
                            </tr>