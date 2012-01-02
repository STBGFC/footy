package org.davisononline.footy.core

import org.davisononline.footy.*

class ChangePasswordTests extends AbstractTestHelper {

    def input(pwd, newPwd, repeat) {
        form.password = pwd
        form.password_new = newPwd
        form.password_new_2 = repeat
        submit.click()
    }

    void testIncorrectCurrentPassword() {
        login ("Manager1", "Manager1")
        changePassword.click()
        input('wrong', 'secret', 'secret')
        assert msg == "Current password is incorrect"
        auth.logout()
    }

    void testNonMatchingNewPasswords() {
        login ("Manager1", "Manager1")
        changePassword.click()
        input('Manager1', 'secret', 'different')
        assert msg == 'Please enter your current password and a valid new password'
        auth.logout()
    }

    void testMinimumPasswordRequirement() {
        login ("Manager1", "Manager1")
        changePassword.click()
        input('Manager1', 'a', 'a')
        assert msg == 'Password does not meet minimum requirements'
        auth.logout()
    }

    void testSuccess() {
        login ("Manager1", "Manager1")
        changePassword.click()
        input('Manager1', 'Manager2', 'Manager2')
        assert msg == 'Password changed successfully'
        auth.logout()
        // change back for other scripts using this login
        login ("Manager1", "Manager2")
        changePassword.click()
        input('Manager2', 'Manager1', 'Manager1')
        assert msg == 'Password changed successfully'
        auth.logout()
    }

}

