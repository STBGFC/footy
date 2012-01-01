package org.davisononline.footy

import geb.Module

/**
 * login/logout funtionality for lots of tests
 */
class AuthModule extends Module {

    static content  = {
        // logged in elements
        username(required: false) { $("#username").text() }
        profileButton(required: false, to: ProfilePage) {
            $('a', text: '[profile]')
        }
        logoutButton(required: false, to: HomePage) { 
            $("a", text:"[logout]") 
        }

        // not logged in elements
        form(required: false) { $("form#login") }
        loginButton(required: false, to: ProfilePage) {
            $("a", text:"login") 
        }
    }

    boolean isLoggedIn() {
        username
    }

    void login(String name, String password) { 
        if (loggedIn) {
            println "Already logged in!!"
            return
        }
        form.j_username = name
        form.j_password = password
        loginButton.click()
        // why does this error in here..?
        //waitFor { at(ProfilePage) }
    }

    void profilePage() {
        if (!loggedIn) {
            println "Need to be logged in to see profile page!"
            return
        }
        profileButton.click()
    }

    void logout() {
        if (!loggedIn) {
            println "Not logged in!" 
            return
        }
        logoutButton.click()
        // as above..
        //waitFor { at(HomePage) }
    }
}

