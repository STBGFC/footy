package org.davisononline.footy.core

import org.davisononline.footy.*
import com.dumbster.smtp.*


class ResetPasswordTests extends AbstractTestHelper {

    def failLogin(name, pwd) {
        go ''
        waitFor { at(HomePage) }
        auth.login(name, pwd)
    }

    def doReset(loginName) {
        $('a', text:'Reset Password').click()
        waitFor { $('input#username') }
        // still have to wait a while for the dialog box to open before we
        // can interact with the field even after rendering the response HTML
        Thread.sleep(2000)
        $('input#username').value(loginName)
        $('input', value: 'Reset').click()
    }

    void testResetPasswordForUnknownUser() {
        // duff login to get the "Auth Required" page
        failLogin("sa", "oops")
        assert msg == "Sorry, we were not able to find a user with that username and password."
        doReset('foo')
        assert msg == 'No such user found'
    }

    void testResetPassword() {

        // start the SMTP server.. ensure test config for the grails mail plugin
        // points to a server on localhost:2525
        def smtp = SimpleSmtpServer.start(2525)

        // duff login to get the "Auth Required" page
        failLogin("sa", "oops")
        assert msg == "Sorry, we were not able to find a user with that username and password."
        doReset('Manager1')
        assert msg == 'An email has been sent to the registered address for this account.'
        assert smtp.receivedEmailSize == 1

        def user = SecUser.findByUsername("Manager1")
        smtp.receivedEmail.each { 
            assert it.body.contains(user.resetToken)
        }
        smtp.stop()

        smtp = SimpleSmtpServer.start(2525)

        // duff token should give 404
        go 'login/reset/deadbeef010101010'
        assert title == '404 - Not Found'

        // find the right token and current (encrypted) password
        def oldPwd = user.password
        def newPwdPlain
        go "login/reset/${user.resetToken}"
        assert $('div.body').text().contains("Password reset successful. A temporary password has been sent to you by email.")
        user.refresh()
        assert user.resetToken == null
        assert smtp.receivedEmailSize == 1
        smtp.receivedEmail.each { 
            assert it.body.contains("You have been assigned a temporary password of")
            // parse the password from the email body so we can use it.  See Config.groovy
            // for explanation of why this oddity works (template coercion!)
            newPwdPlain = it.body[0..it.body.indexOf('###')-1]
        }
        smtp.stop()

        // change the pwd again in the db (no way to know what was changed to).  Should
        // still force change at first login
        assert user.password != oldPwd

        go ''
        waitFor { at(HomePage) }
        auth.login('Manager1', newPwdPlain)
        waitFor { at(ChangePasswordPage) }
        assert msg == "Sorry, your password has expired."
        auth.logout()

        // finally, remove the requirement to change password
        user.passwordExpired = false
        user.password = oldPwd
        user.save(flush:true)
        login('Manager1', 'Manager1') // <-- will waitFor profile page
        auth.logout()
    }
}

