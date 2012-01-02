package org.davisononline.footy

class ChangePasswordPage extends FootyPage {
    static at = { 
        title == "Change Password" 
    }
    static content = {
        form { $('form#passwordResetForm') }
        submit { $('input', value:'Reset Password') }
    }
}

