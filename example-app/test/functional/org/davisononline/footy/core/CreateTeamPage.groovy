package org.davisononline.footy.core

import geb.Page
import org.davisononline.footy.*


class CreateTeamPage extends Page {
    static content = {
        teamForm { $("form#team") }
        manager { $("form#team").find("select", name:"manager.id") }
        crud { module CrudModule, titleText: "Add New Team" }
        auth { module AuthModule }
    }
}

