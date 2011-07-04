package org.davisononline.footy.core

import org.davisononline.footy.ListPage

class TeamListPage extends ListPage {
    static at = { title == "Team List" }
    static content = {
        newTeamButton(to: CreateTeamPage) { 
            $("a", text: "New Team") 
        }
    }
}

