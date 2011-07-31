<g:each in="${teams}" var="team" status="i">
    <g:checkBox name="chkTeam" checked="${i==0}" value="${team.id}"/>&nbsp;${team.name}
</g:each>
<g:if test="${teams.size()>1}"><br/>
    <input
            type="button"
            onclick="c=document.email.chkTeam;for(var i=0; i<c.length; i++){c[i].checked=true}"
            value="check all"
    />
</g:if>