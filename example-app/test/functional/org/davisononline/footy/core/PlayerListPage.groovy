package org.davisononline.footy.core

import org.davisononline.footy.ListPage

class PlayerListPage extends ListPage {
    static at = { title == "Player List" }
    static content = {
        newPlayerButton(to: CreatePlayerPage) { 
            $("a", text: "New Player") 
        }
        personListButton { $("a", name: "People List") }
    }
}
