package org.davisononline.footy.core

import org.davisononline.footy.*

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
        // duff login to get the "Auth Required" page
        failLogin("sa", "oops")
        assert msg == "Sorry, we were not able to find a user with that username and password."
        doReset('Manager1')
        assert msg == 'An email has been sent to the registered address for this account.'

        // duff token should give 404
        go 'login/reset/deadbeef010101010'
        assert title == '404 - Not Found'

        // find the right token and current (encrypted) password
        def user = SecUser.findByUsername("Manager1")
        def oldPwd = user.password
        go "login/reset/${user.resetToken}"
        assert $('div.body').text().contains("Password reset successful. A temporary password has been sent to you by email.")
        user.refresh()
        assert user.resetToken == null

        // change the pwd again in the db (no way to know what was changed to).  Should
        // still force change at first login
        assert user.password != oldPwd
        user.password = oldPwd
        user.save(flush:true)

        go ''
        waitFor { at(HomePage) }
        auth.login('Manager1', 'Manager1')
        waitFor { at(ChangePasswordPage) }

        // finally, remove the requirement to change password
        user.passwordExpired = false
        user.save(flush:true)
    }
}

