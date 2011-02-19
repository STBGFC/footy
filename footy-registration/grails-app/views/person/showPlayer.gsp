
<%@ page import="leagueman.*" %>
    <table id="playerDetail"><tbody><tr>
        <td>  
            <h2>Player Details</h2>
            <ul id="playerDetails" class="clearList">
                <li>Full Name: <strong>${person.fullName().encodeAsHTML()}</strong></li>
                <li>DoB: <strong><g:formatDate format="yyyy-MM-dd" date="${person.dateOfBirth}"/></strong></li>
                <li>Team: <strong>${person.team.encodeAsHTML()}</strong></li>
                <li>Registration ID: <strong>${person.leagueRegistrationNumber?.encodeAsHTML() ?: 'NOT REGISTERED'}</strong></li>
            </ul>
            
            <div>Notes: (<a href="#" id="editNotes${person.id}">edit</a>)</div>
            <p class="playerNotes" id="playerNotes${person.id}">${person?.notes.encodeAsHTML()}</p>
            <gui:dialog
                title="Update Player Notes"
                height="240"
                width="420"
                form="true"
                controller="person"
                action="updateNotes"
                update="playerNotes${person.id}"
                modal="true"
                triggers="[show:[id:'editNotes'+person.id, on:'click']]"
                >
                <g:hiddenField name="id" value="${person.id}"/>
                <g:textArea name="notes" value="${person.notes}"/>
            </gui:dialog>
            
            <g:if test="${person?.guardian}">
            <h2>Parent/Guardian</h2>
            <tmpl:personTemplate person="${person?.guardian}"/>
            <span class="menuButton"><g:link class="edit" controller="person" action="edit" id="${person.id}">edit ${person}</g:link></span>   
            </g:if>
        </td>
        <td>
            <g:render template="/shared/addressMap" model="[person:person?.guardian, club:person.club]"/>
        </td>
    </tr></tbody></table>
