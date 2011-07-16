package org.davisononline.footy.core

import geb.Page
import org.davisononline.footy.*

/**
 * create/edit form for Person
 */
class CreatePersonPage extends Page {
    static at = {
        $("form", id:"personEditForm")
    }
    static content = {
        addQual { $("a", text: "Add New") }
        qualForm { $("form#addQualification") }
        qual { $("form#addQualification").find("select", name: "type.id") }
        qualSubmit { $("input", value: "Add") }
        qualList { $("td#qualifications").find("ul").find("li") }

        person { module PersonFormModule, formName:'personEditForm' }
        crud { module CrudModule }
        auth { module AuthModule }
    }
}

