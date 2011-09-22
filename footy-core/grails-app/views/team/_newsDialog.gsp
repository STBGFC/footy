<p>
    <g:message
        code="org.davisononline.footy.core.addnews.dialog"
        default="Add a news item to the team page"/>
</p>
<g:form name="news" action="addNews" method="post">
    <p>
        <strong><g:message
            code="org.davisononline.footy.core.sendmessage.subject"
            default="Subject:"/></strong><br/>
        <g:textField name="subject" maxlength="50" style="width:390px"/>
    </p>
    <p>
        <g:textArea name="body" rows="20" cols="60" style="width:390px;height:190px">your news here...</g:textArea>
    </p>
    <p>
        <g:checkBox name="clubWide" /> add to site home page<br/>
        (only tick the box above if your news is of interest to the whole club!)
        <g:hiddenField name="team.id" value="${id}"/>
    </p>
    <input type="submit" value="Create" />
</g:form>
