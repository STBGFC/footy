<p>
    <g:message
        code="org.davisononline.footy.core.team.uploadphoto.dialog"
        default="Upload your team photo here - ensure that it has a size of 240x180 pixels (WxH), it will be scaled to this size if not."/>
</p>
<g:form action="photoUpload" method="post" enctype="multipart/form-data">
    <p><input type="file" name="photo" /></p>
    <p>
        <g:checkBox name="delete" checked="false" value="true"></g:checkBox>
        <g:message
            code="org.davisononline.footy.core.team.uploadphoto.dialog.removepicture"
            default="Remove current picture"/>
    </p>
    <g:hiddenField name="id" value="${id}"/>
    <input type="submit" value="Upload or Delete"/>
</g:form>