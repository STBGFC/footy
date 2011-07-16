package org.davisononline.footy

import geb.Module

/**
 * module for PersonForm pages
 */
class PersonFormModule extends Module {

    def  formName

    static content = {
        personForm { $("form", name:formName) }
        house { personForm.find("input", name:"address.house") }
        address { personForm.find("input", name:"address.address") }
        postCode { personForm.find("input", name:"address.postCode") }
    }
}

