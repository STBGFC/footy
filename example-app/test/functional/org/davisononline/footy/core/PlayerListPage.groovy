package org.davisononline.footy.core

import org.davisononline.footy.ListPage

class PlayerListPage extends ListPage {
    static at = { title == "Player List" }
    static content = {
        personListButton { $("a", name: "People List") }
    }
}
