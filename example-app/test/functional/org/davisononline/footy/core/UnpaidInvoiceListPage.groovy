package org.davisononline.footy.core

import org.davisononline.footy.ListPage

class UnpaidInvoiceListPage extends ListPage {
    static at = { title == "Unpaid Invoices List" }
    static content = {
        unpaidButton(to: UnpaidInvoiceListPage) { 
            $("a", text: "Payment Reconciliation") 
        }
    }
}


