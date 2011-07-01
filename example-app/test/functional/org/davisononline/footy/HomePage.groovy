package org.davisononline.footy

import geb.Page

/**
 * home page for the example app under test
 */
class HomePage extends Page { 
    static url = ""
    static at = { title == "Example FC Registrations and Payments" }
    static content = {
        auth { module AuthModule }
    }
}

