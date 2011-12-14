<%@ page import="org.codehaus.groovy.grails.commons.ConfigurationHolder; org.davisononline.footy.core.Sponsor" %>
<p>
    <strong>
    <g:message
        code="org.davisononline.footy.core.team.editsponsor.dialog.selectlist"
        default="Select your sponsor from the list if it exists already.  If not, please contact the site administrators with details of the new sponsor"/>
    </strong>
</p>
<g:form action="assignSponsor" method="post" enctype="multipart/form-data">
    <g:hiddenField name="id" value="${id}"/>
    <p>
        <g:select
            name="sponsor.id"
            from="${Sponsor.listOrderByName()}"
            optionKey="id"
            value="${teamInstance?.sponsor?.id}"
            noSelection="${['null':'-- none --']}"
        />
    <input type="submit" value="Select This Sponsor"/>
    </p>
</g:form>
