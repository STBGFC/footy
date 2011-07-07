<html>
	<head>
		<meta http-equiv="Content-type" content="text/html; charset=utf-8">
		<title><g:message code="org.davisononline.footy.core.title.changepassword"
            default="Change Password"/></title>
	</head>
    <body>
        <div class="dialog">
            <p>
                <g:message code="org.davisononline.footy.core.body.changepassword"
                    default="Password can be changed here"/>
            </p>

            <g:form action='updatePassword' id='passwordResetForm' autocomplete='off'>
            <table>
                <tbody>
                <tr class="prop">
                    <td  class="name">
                        <label for="username"><g:message code="org.davisononline.org.footy.core.username.label" default="User Name" /></label>
                    </td>
                    <td  class="value ${hasErrors(bean: personCommand, field: 'familyName', 'errors')}">
                        <strong>${username}</strong>
                    </td>
                </tr>
                <tr class="prop">
                    <td  class="name">
                        <label for="password"><g:message code="org.davisononline.org.footy.core.password.label" default="Current Password" /></label>
                    </td>
                    <td  class="value">
                        <g:passwordField name='password'/>
                    </td>
                </tr>
                <tr class="prop">
                    <td  class="name">
                        <label for="password_new"><g:message code="org.davisononline.org.footy.core.passwordNew.label" default="New Password" /></label>
                    </td>
                    <td  class="value">
                        <g:passwordField name='password_new'/><br/>
                        <g:message code="org.davisononline.org.footy.core.passwordConstraints.label"
                                default="Please use a password with at least one upper case letter, one lower case letter and one digit.  It must be 8 or more characters long"/>
                    </td>
                </tr>
                <tr class="prop">
                    <td  class="name">
                        <label for="password_new_2"><g:message code="org.davisononline.org.footy.core.passwordNew2.label" default="Repeat New Password" /></label>
                    </td>
                    <td  class="value">
                        <g:passwordField name='password_new_2'/>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="buttons">
            <span class="button"><input class="save" type="submit" value="${message(code: 'default.button.resetpassword.label', default: 'Reset Password')}" /></span>
        </div>
        </g:form>
	</body>

</html>