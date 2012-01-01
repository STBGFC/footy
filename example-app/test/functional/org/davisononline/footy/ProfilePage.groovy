package org.davisononline.footy

import org.davisononline.footy.core.*

class ProfilePage extends FootyPage {

    static at = { $('h2', text:'Pages and Links') }

    static content = {

        // admin links
        personList(required: false, to: PersonListPage) { 
            $("a", text: "Member List") 
        }
        playerList(required: false, to: PlayerListPage) { 
            $("a", text: "Player List") 
        }
        teamList(required: false, to: TeamListPage) { 
            $("a", text: "Team List") 
        }
        /*
        qualificationList(to: QualificationsExpiringListPage) { 
            $("a", text: "Qualifications expiring soon") 
        }
        */
        invoices(required: false, to: InvoiceListPage) { 
            $("a", text: "Payment Reconciliations") 
        }
    }
}

