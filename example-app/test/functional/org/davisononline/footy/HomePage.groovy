package org.davisononline.footy

import geb.Page
import org.davisononline.footy.core.*


/**
 * home page for the example app under test
 */
class HomePage extends Page { 
    static url = ""
    static at = { title == "Example FC Registrations and Payments" }
    static content = {
        auth { module AuthModule }

        // admin links
        personList(to: PersonListPage) { 
            $("a", text: "People") 
        }
        playerList(to: PlayerListPage) { 
            $("a", text: "Players") 
        }
        teamList(to: TeamListPage) { 
            $("a", text: "Teams") 
        }
        /*
        qualifications(to: QualificationsExpiringListPage) { 
            $("a", text: "Qualifications Expiring Soon") 
        }
        */
        invoices(to: InvoiceListPage) { 
            $("a", text: "Payment Reconciliations") 
        }
    }
}

