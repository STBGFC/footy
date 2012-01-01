package org.davisononline.footy

import org.davisononline.footy.core.*


/**
 * home page for the example app under test
 */
class HomePage extends FootyPage { 
    static url = ""
    static at = { title == "Example FC Registrations and Payments" }
    static content = {

        // register player link
        registerPlayer() {
            $("a", text: "Register Player")
        }

    }
}

