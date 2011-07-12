package org.davisononline.footy

import geb.Module

/**
 * module for CRUD pages
 */
class CrudModule extends Module {
    
    def titleText

    static at = { title == titleText }

    static content =  {
        saveButton { $("input", value: "Save") }
        flash(required: false) { $("div.message") }
    }
}

