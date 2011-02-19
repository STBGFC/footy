<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'person.label', default: 'Personal Details')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="dialog">
        <g:form method="post" >
            <g:hiddenField name="id" value="${p?.id}" />
            <g:hiddenField name="version" value="${p?.version}" />
            <table>
                <tbody>
                
                    <tr class="prop">
                        <td class="name">
                          <label for="familyName"><g:message code="person.familyName.label" default="Family Name" /></label>
                        </td>
                        <td class="value ${hasErrors(bean: p, field: 'familyName', 'errors')}">
                            <g:textField name="familyName" maxlength="50" value="${p?.familyName}" />
                        </td>
                    </tr>
                
                    <tr class="prop">
                        <td class="name">
                          <label for="givenName"><g:message code="person.givenName.label" default="Given Name" /></label>
                        </td>
                        <td class="value ${hasErrors(bean: p, field: 'givenName', 'errors')}">
                            <g:textField name="givenName" maxlength="50" value="${p?.givenName}" />
                        </td>
                    </tr>
                
                    <tr class="prop">
                        <td class="name">
                          <label for="knownAsName"><g:message code="person.knownAsName.label" default="Known As Name" /></label>
                        </td>
                        <td class="value ${hasErrors(bean: p, field: 'knownAsName', 'errors')}">
                            <g:textField name="knownAsName" value="${p?.knownAsName}" />
                        </td>
                    </tr>
                    
                    <g:if test="${!p.instanceOf(Player)}">
                    <!-- Person (non-player) fields -->
                    <tr class="prop">
                        <td class="name">
                          <label for="email"><g:message code="person.email.label" default="Email" /></label>
                        </td>
                        <td class="value ${hasErrors(bean: p, field: 'email', 'errors')}">
                            <g:textField name="email" value="${p?.email}" />
                        </td>
                    </tr>
                
                    <tr class="prop">
                        <td class="name">
                          <label for="address"><g:message code="person.address.label" default="Address" /></label>
                        </td>
                        <td class="value ${hasErrors(bean: p, field: 'address', 'errors')}">
                            <g:select name="address.name" from="${Address.TYPES}" value="${p?.address?.name}"/><br/>
                            <span class="value ${hasErrors(bean: p.address, field: 'address', 'errors')}"><g:textField name="address.address" value="${p?.address?.address}" /></span><br/>
                            <span class="value ${hasErrors(bean: p.address, field: 'town', 'errors')}"><g:textField name="address.town" value="${p?.address?.town}" /></span> (<g:message code="address.town.label" default="Town" />)<br/>
                            <span class="value ${hasErrors(bean: p.address, field: 'postCode', 'errors')}"><g:textField name="address.postCode" value="${p?.address?.postCode}" /></span> (<g:message code="address.postCode.label" default="Post Code" />)
                        </td>
                    </tr>
                
                    <tr class="prop">
                        <td class="name">
                          <label for="phone1"><g:message code="person.phone1.label" default="Phone1" /></label>
                        </td>
                        <td class="value ${hasErrors(bean: p, field: 'phone1', 'errors')}">
                            <g:textField name="phone1" value="${p?.phone1}" />
                        </td>
                    </tr>
                
                    <tr class="prop">
                        <td class="name">
                          <label for="phone2"><g:message code="person.phone2.label" default="Phone2" /></label>
                        </td>
                        <td class="value ${hasErrors(bean: p, field: 'phone2', 'errors')}">
                            <g:textField name="phone2" value="${p?.phone2}" />
                        </td>
                    </tr>
                    
                    <!-- Qual fields -->
                    <%--   
                    <g:if test="${p?.id}">
                    <tr class="prop">
                        <td class="name">
                            <label for="qualifications"><g:message code="coach.qualifications.label" default="Coaching Qualifications" /></label>
                        </td>
                        <td class="value ${hasErrors(bean: p, field: 'qualifications', 'errors')}">
                            <tmpl:qualifications p="${p}"/>
                            <a href="#" id="addQual"><g:message code="coach.addQualification.label" default="Add Qualification"/></a>
                        </td>
                    </tr>   
                    </g:if>
                     --%>
                    </g:if>
                    
                    <g:if test="${p.instanceOf(Player)}">
                    <!-- Player fields -->                
                    <tr class="prop">
                        <td class="name">
                          <label for="dateOfBirth"><g:message code="player.dateOfBirth.label" default="Date Of Birth" /></label>
                        </td>
                        <td class="value ${hasErrors(bean: p, field: 'dateOfBirth', 'errors')}">
                            <gui:datePicker id='dateOfBirth' value="${p?.dateOfBirth}" formatString="yyyy-MM-dd"/>
                        </td>
                    </tr>    
                
                    <tr class="prop">
                        <td class="name">
                          <label for="guardian"><g:message code="player.guardian.label" default="Guardian" /></label>
                        </td>
                        <td class="value ${hasErrors(bean: p, field: 'guardian', 'errors')}">
                           <g:select name="guardian.id" from="${guardians}" optionKey="id" optionValue="${{it.sortedName() + ', ' + (it.address ?: 'address unknown')}}" value="${p?.guardian?.id}" noSelection="['-1': '-- Not Listed --']" />
                        </td>
                    </tr>   
                
                    <tr class="prop">
                        <td class="name">
                          <label for="secondGuardian"><g:message code="player.secondGuardian.label" default="Other Guardian" /></label>
                        </td>
                        <td class="value ${hasErrors(bean: p, field: 'secondGuardian', 'errors')}">
                           <g:select name="secondGuardian.id" from="${guardians}" optionKey="id" optionValue="${{it.sortedName() + ', ' + (it.address ?: 'address unknown')}}" value="${p?.secondGuardian?.id}" noSelection="['null': '-- N/A --']" />
                        </td>
                    </tr>
                
                    <tr class="prop">
                        <td class="name">
                          <label for="team"><g:message code="player.team.label" default="Team" /></label>
                        </td>
                        <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_CLUB_ADMIN">
                        <td class="value ${hasErrors(bean: p, field: 'team', 'errors')}">
                            <g:select name="team.id" from="${p.club.teams}" optionKey="id" value="${p?.team?.id}"  />
                        </td>
                        </sec:ifAnyGranted>
                        <sec:ifNotGranted roles="ROLE_ADMIN,ROLE_CLUB_ADMIN">
                        <td class="value ${hasErrors(bean: p, field: 'team', 'errors')}">
                            ${p.team}
                        </td>
                        </sec:ifNotGranted>
                    </tr>
                        
                    <tr class="prop">
                        <td class="name">
                          <label for="dateJoinedClub"><g:message code="player.dateJoinedClub.label" default="Date Joined Club" /></label>
                        </td>
                        <g:if test="${p.id==null}">
                        <td class="value ${hasErrors(bean: p, field: 'dateJoinedClub', 'errors')}">
                            <gui:datePicker id='dateJoinedClub' value="${p?.dateJoinedClub}" formatString="yyyy-MM-dd"/>
                        </td>
                        </g:if>
                        <g:else>
                        <td class="value">
                            <g:formatDate date="${p?.dateJoinedClub}" format="yyyy"/>
                        </td>                        
                        </g:else>
                    </tr>
                
                    <tr class="prop">
                        <td class="name">
                          <label for="leagueRegistrationNumber"><g:message code="player.registrationNumber.label" default="League Registration #" /></label>
                        </td>
                        <td class="value ${hasErrors(bean: p, field: 'leagueRegistrationNumber', 'errors')}">
                            <g:textField name="leagueRegistrationNumber" value="${p?.leagueRegistrationNumber}" />
                        </td>
                    </tr>
                
                    <tr class="prop">
                        <td class="name">
                          <label for="notes"><g:message code="player.notes.label" default="Notes" /></label>
                        </td>
                        <td class="value ${hasErrors(bean: p, field: 'notes', 'errors')}">
                            <g:textArea name="notes" value="${p?.notes}" />
                        </td>
                    </tr>
                    </g:if>
                    <!-- end player fields -->
                
                </tbody>
            </table>
            <div class="buttons">
                <span class="button">
                    <g:if test="${p.id}">
                    <g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                    </g:if>
                    <g:else>
                    <g:actionSubmit class="save" action="save" value="${message(code: 'default.button.save.label', default: 'Save')}" />
                    </g:else>
                </span>
                <span class="button">
                    <g:actionSubmit class="cancel" action="cancel" value="${message(code: 'default.button.cancel.label', default: 'Cancel')}" />
                </span>
            </div>
        </g:form>
        <%--
        <gui:dialog
            title="New Coaching Qualification"
            height="180"
            width="300"
            modal="true"
            form="true"
            controller="person"
            update="coachDetails${p.id}"
            action="addQualification"
            triggers="[show:[id:'addQual', on:'click']]"
            >
                Date Attained: <gui:datePicker id='dateAttained' value="${new Date()}" formatString="yyyy-MM-dd"/>
                Type:<br/><g:select name="type.id" from="${QualificationType.listOrderByName()}" optionKey="id"/>
                <g:hiddenField name="id" value="${p.id}"/>
        </gui:dialog>
         --%>
    </body>
</html>
