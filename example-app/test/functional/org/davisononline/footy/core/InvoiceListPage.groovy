package org.davisononline.footy.core

import org.davisononline.footy.ListPage

class InvoiceListPage extends ListPage {
    static at = { title == "Payment Reconciliations List" }
    static content = {
        unpaidButton(to: UnpaidInvoiceListPage) { 
            $("a", text: "Unpaid Invoices") 
        }
    }
}

