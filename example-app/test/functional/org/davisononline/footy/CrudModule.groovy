package org.davisononline.footy

import geb.Module

/**
 * module for CRUD pages
 */
class CrudModule extends Module {
    
    def titleText
    def label = "Continue"

    static at = { title == titleText }

    static content =  {
        errors { $("div.errors") }
        error { i -> errors[i] }        
        contButton { $("input", value:label) }
        flash(required: false) { $("div.message") }
    }
}

