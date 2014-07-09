<%@ page import="org.davisononline.footy.core.*" %>

                            <r:script disposition="head">
                            function updateDiv(e) {
                                var divisions = eval("(" + e.responseText + ")")	// evaluate JSON returned from ajax call

                                if (divisions) {
                                    var rselect = document.getElementById('division.id')

                                    // Clear all previous options
                                    var l = rselect.length

                                    while (l > 1) {
                                        l--
                                        rselect.remove(l)
                                    }

                                    // Rebuild the select
                                    for (var i=0; i < divisions.length; i++) {
                                        var division = divisions[i]
                                        var opt = document.createElement('option');
                                        opt.text = division.name
                                        opt.value = division.id
                                        try {
                                            rselect.add(opt, null) // standards compliant; doesn't work in IE
                                        }
                                        catch(ex) {
                                            rselect.add(opt) // IE only
                                        }
                                    }
                                }
                            }
                            </r:script>
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
                                    <label for="ageGroup.id"><g:message code="entry.ageGroup.label" default="Age Group" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: teamCommand, field: 'ageGroup', 'errors')}">
                                    <g:select class="short" name="ageGroup.id" from="${AgeGroup.list()}" value="${teamCommand?.ageGroup?.id}" optionKey="id" />
                                    <br/>
                                    <g:checkBox name="girlsTeam" value="${teamCommand?.girlsTeam}" /> (Girls)
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
                                    <g:select name="league.id" from="${League.list()}" value="${teamCommand?.league?.id}" optionValue="name" optionKey="id" valueMessagePrefix="entry.league"
                                              onchange="${remoteFunction(
                                                    controller:'league',
                                                    action:'divisionsInLeague',
                                                    params:'\'id=\' + escape(this.value)',
                                                    onComplete:'updateDiv(e)')}"
                                     />
                                    <g:render template="/shared/fieldError" model="['instance':teamCommand,'field':'league']" plugin="footy-core"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td  class="name">
                                    <label for="division.id"><g:message code="entry.division.label" default="Division" /></label>
                                </td>
                                <td  class="value ${hasErrors(bean: teamCommand, field: 'division', 'errors')}">
                                    <g:select name="division.id" from="${teamCommand?.league?.divisions ?: League.list()[0].divisions}" value="${teamCommand?.division?.id}" optionKey="id" noSelection="${['null':'-- none / not listed --']}"/>
                                    <g:render template="/shared/fieldError" model="['instance':teamCommand,'field':'division']" plugin="footy-core"/>
                                </td>
                            </tr>