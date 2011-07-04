package org.davisononline.footy.core

import org.davisononline.footy.ListPage

class PersonListPage extends ListPage {
    static at = { title == "Person List" }
    static content = {
        newPersonButton(to: CreatePersonPage) { 
            $("a", text: "New Person") 
        }
        playerListButton { $("a", name: "Player List") }
    }
}

