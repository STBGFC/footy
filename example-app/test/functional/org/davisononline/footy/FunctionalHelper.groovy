package org.davisononline.footy

import functionaltestplugin.FunctionalTestCase

class FunctionalHelper extends FunctionalTestCase {

    void setUp() {
        super.setUp()
        // framework should set this but seems not to
        System.properties['grails.run.mode'] = 'functional-test'
    }

    /**
     * login with the supplied name/pwd combo
     */
    void login(name, pwd) {
        get ('/')
        form('login') {
            j_username name
            j_password pwd
            click "loginSubmit"
        }

        assertStatus 200
        assertContentContains "Logged in"
        assertContentContains "[logout]"
    }

}

