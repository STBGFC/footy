package org.davisononline.footy.core

import geb.Page
import org.davisononline.footy.*

/**
 * create/edit form for Person
 */
class CreatePersonPage extends Page {
    static at = {
        $("form#personEditForm")
    }
    static content = {
        personForm { $("form#personEditForm") }
        house { personForm.find("input", name:"address.house") }
        address { personForm.find("input", name:"address.address") }
        postCode { personForm.find("input", name:"address.postCode") }
        addQual { $("a", text: "Add New") }
        qualForm { $("form#addQualification") }
        qual { $("form#addQualification").find("select", name: "type.id") }
        qualSubmit { $("input", value: "Add") }
        qualList { $("td#qualifications").find("ul").find("li") }

        crud { module CrudModule }
        auth { module AuthModule }
    }
}

