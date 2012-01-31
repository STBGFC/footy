<html>
	<head>
		<meta http-equiv="Content-type" content="text/html; charset=utf-8">
		<title><g:message code="org.davisononline.footy.core.title.auth"
            default="Authentication Required"/></title>
	</head>
    <body>
        <%--
            simple auth form that should be overridden by individual applications not already
            providing alternative login forms or ajax auth
        --%>
        <h1>
            <g:message code="org.davisononline.footy.core.title.auth" default="Authentication Required"/>
        </h1>
        <p>
            <g:message code="org.davisononline.footy.core.body.auth"
                default="Please login to access this page."/>
        </p>
        <p>
            <g:message code="org.davisononline.footy.core.body.forgottenpassword"
                default="If you have forgotten your password, click the link below in order to reset it."/>
        </p>
        <p>
            <modalbox:createLink
                    controller="login"
                    action="resetPassword"
                    title="Reset Password"
                    width="300">
                <g:message code="org.davisononline.footy.core.link.resetpassword" default="Reset Password"/>
            </modalbox:createLink>
        </p>

        <div class="clearer">&nbsp;</div>

	</body>

</html>
