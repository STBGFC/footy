<p>
    <g:message
        code="org.davisononline.footy.core.person.uploadphoto.dialog"
        default="Upload your photo here - ensure that it has a size of 88x66 pixels (WxH), it will be scaled to this size if not."/>
</p>
<g:form action="photoUpload" method="post" enctype="multipart/form-data">
    <p><input type="file" name="photo" /></p>
    <p>
        <g:checkBox name="delete" checked="false" value="true"></g:checkBox>
        <g:message
            code="org.davisononline.footy.core.person.uploadphoto.dialog.removepicture"
            default="Remove current picture"/>
    </p>
    <g:hiddenField name="id" value="${id}"/>
    <g:hiddenField name="teamId" value="${teamId}"/>
    <input type="submit" value="Upload or Delete"/>
</g:form>