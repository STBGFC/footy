<html>
	<head>
		<meta http-equiv="Content-type" content="text/html; charset=utf-8">
		<title><g:message code="org.davisononline.footy.core.title.auth"
            default="Authentication Required"/></title>
	</head>
    <body>
        <g:if test="${failureMessage}">
        <h1>
            <g:message code="org.davisononline.footy.core.title.authfail" default="Authentication Failed"/>
        </h1>
        <p class="notice largest">
            ${failureMessage}
        </p>
        </g:if>
        <g:else>
        <h1>
            <g:message code="org.davisononline.footy.core.title.auth" default="Authentication Required - Please Login"/>
        </h1>
        </g:else>
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
