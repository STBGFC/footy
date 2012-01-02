package org.davisononline.footy.core

import org.davisononline.footy.*

class ChangePasswordTests extends AbstractTestHelper {

    void testIncorrectPasswords() {
        login ("manager1", "manager1")
        changePassword.click()
        currentPwd = "wrong"
        newPwd = "secret"
        newPwdRepeat = "secret"
        submit.click()
        assert msg == "Current password is incorrect"
    }
}

