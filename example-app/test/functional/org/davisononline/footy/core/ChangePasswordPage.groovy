package org.davisononline.footy

class ChangePasswordPage extends FootyPage {
    static at = { title == "Change Password" }
    static content = {
        currentPwd { $('input#password') }
        newPwd { $('input#password_new') }
        newPwdRepeat { $('input#password_new_2') }
        submit { $('input', value:'Reset Password') }
    }
}

