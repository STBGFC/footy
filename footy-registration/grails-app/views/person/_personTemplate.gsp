
    <ul id="contactDetails" class="clearList">
        <li>Full Name: <strong>${person.fullName()}</strong></li>
        <g:if test="${person?.email}">
        <li><g:message code="person.email.label" default="email" />: <strong><a href="mailto:${person.email}">${person.email}</a></strong></li>
        </g:if>
        
        <g:if test="${person?.phone1}">
        <li><g:message code="person.phone1.label" default="Mobile" />: <strong>${person?.phone1}</strong></li>
        </g:if>
        
        <g:if test="${person?.phone2}">
        <li><g:message code="person.phone2.label" default="Tel (alt)" />: <strong>${person?.phone2}</strong></li>
        </g:if>
        
        <g:if test="${person?.address}">
        <li><g:message code="person.address.label" default="Address" />: <address>${person?.address}</address></li>
        </g:if>
    </ul>
    <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_COACH">
    <span class="menuButton"><g:link class="edit" controller="person" action="edit" id="${person.id}">edit ${person}</g:link></span>
    </sec:ifAnyGranted>
    