<p>
    <g:message
        code="org.davisononline.footy.core.sendmessage.dialog"
        default="Send messages to various coaches or parents from here."/>
</p>
<g:form name="email" action="sendMessage" method="post">
    <p>
        <strong><g:message
            code="org.davisononline.footy.core.sendmessage.to"
            default="First, select which age group your message will go to:"/></strong><br/>
        <g:select
                name="ageBand"
                from="${ages}"
                optionValue="${{'U' + it}}"
                noSelection="${['0':'ALL TEAMS']}"
                style="width: 100px"
                onchange="${remoteFunction(
                    controller:'team',
                    action:'teamsForAgeBand',
                    params:'\'ageBand=\' + escape(this.value)',
                    onComplete:'l=$(\'msgDlgTeamList\');l.innerHTML=e.responseText;Effect.Pulsate(l,{pulses:2, duration:0.7})')}"
            />
        <div id="msgDlgTeamList"style="height:45px">
            Email will go to ALL TEAMS in the database.  Select an age group above to narrow your distribution
        </div>
    </p>
    <p>
        <strong><g:message
            code="org.davisononline.footy.core.sendmessage.to"
            default="Who within those teams:"/></strong><br/>
        <g:each in="['managers', 'managers and coaches', 'all parents']" status="i" var="r">
            <g:radio value="${i}" name="to" checked="${i==0 ? 'checked':''}"/> ${r}
        </g:each>
    </p>
    <p>
        <strong><g:message
            code="org.davisononline.footy.core.sendmessage.subject"
            default="Subject:"/></strong><br/>
        <g:textField name="subject" style="width:390px"/>
    </p>
    <p>
        <g:textArea name="body" rows="20" cols="60" style="width:390px;height:190px">your message here...</g:textArea>
    </p>
    <input type="submit" value="Send" />
</g:form>
