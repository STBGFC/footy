package org.davisononline.footy

import geb.Module

/**
 * login/logout funtionality for lots of tests
 */
class AuthModule extends Module {

    static content  = {
        // logged in elements
        username(required: false) { $("#username").text() }
        logoutButton(required: false) { $("a", text:"[logout]") }

        // not logged in elements
        form(required: false) { $("form#login") }
        loginButton(required: false) { $("a", text:"login") }
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

