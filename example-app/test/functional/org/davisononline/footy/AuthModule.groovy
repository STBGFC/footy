package org.davisononline.footy

import geb.Module

class AuthModule extends Module {

    static content  = {
        // logged in elements
        username(required: false) { $("#username").text() }
        logoutButton(required: false) { $("a[name='[logout]']") }

        // not logged in elements
        form(required: false) { $("form#login") }
        loginButton(required: false) { $("a[name=login]") }
    }

    boolean isLoggedIn() {
        username
    }

    void login(String name, String password) { 
        if (loggedIn)
            throw new IllegalStateException("Already logged in!")
        form.j_username = name
        form.j_password = password
        loginButton.click()
    }

    void logout() {
        if (!loggedIn)
            throw new IllegalStateException("Not logged in!")
        logoutButton.click()
    }
}

