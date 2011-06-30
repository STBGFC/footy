package org.davisononline.footy.core

import geb.Browser
import geb.Page
import geb.Module
import org.davisononline.footy.*

class FlowModule extends Module {
    static content =  {
        errors { $("div.errors") }
        error { i -> errors[i] }        
        contButton { $("input", value:"Continue") }
    }
}

class TierPage extends Page {
    static at = { title == "Registration Type" }
    static content = {
        flow { module FlowModule }
    }
}

class PlayerPage extends Page {
    static at = { title == "Junior Registration" }
    static content = {
        playerForm { $("form#registration") }
        givenName { playerForm.find("input", name:"person.givenName") }
        familyName { playerForm.find("input", name:"person.familyName") }
        flow { module FlowModule }
    }
}

class PersonPage extends Page {
    static at = { title == "Enter Parent/Guardian Details" }
    static content = {
        personForm { $("form#registration") }
        house { personForm.find("input", name:"address.house") }
        address { personForm.find("input", name:"address.address") }
        postCode { personForm.find("input", name:"address.postCode") }
        flow { module FlowModule }
    }
}

class TeamPage extends Page {
    static at = { title == "Assign Team" }
    static content = {
        flow { module FlowModule }
    }
}

class InvoicePage extends Page {
    static at = { title == "Payment" }
    static content = {
        lineItems { $("tr.invoiceBody") }
        lineItem { i -> lineItems[i] }
        itemName { n -> lineItem(n).find("td", 1).text() }
        itemAmount { n -> lineItem(n).find("td.amount").text() }
    }
}

class RegistrationTests extends AbstractTestHelper {

    void testRegistration() {
        go "registration"

        // redirects to first page of flow.
        waitFor { at(TierPage) }
        assert at(TierPage)
        flow.contButton.click()

        // check validations on player page
        waitFor { at(PlayerPage) }
        flow.contButton.click()
        assert flow.errors.size() == 3
        assert flow.error(0).text() == "Property [givenName] of class [class org.davisononline.footy.core.Person] cannot be blank"
        assert flow.error(1).text() == "Property [familyName] of class [class org.davisononline.footy.core.Person] cannot be blank"
        assert flow.error(2).text() == "Property [medical] of class [class org.davisononline.footy.core.Player] cannot be blank"

        // fill in player details
        playerForm.medical = "A bit deaf"
        givenName.value("Joe")
        familyName.value("Bloggs")
        flow.contButton.click()
        waitFor { at(PersonPage) }

        // check validation
        flow.contButton.click()
        assert flow.errors.size() == 7
        assert flow.error(0).text() == "Property [givenName] of class [class org.davisononline.footy.core.Person] cannot be blank"
        assert flow.error(1).text() == "Property [phone1] of class [class org.davisononline.footy.core.Person] with value [null] does not pass custom validation"
        assert flow.error(2).text() == "Property [phone2] of class [class org.davisononline.footy.core.Person] with value [null] does not pass custom validation"
        assert flow.error(3).text() == "Property [email] of class [class org.davisononline.footy.core.Person] cannot be blank"
        assert flow.error(4).text() == "Property [house] of class [class org.davisononline.footy.core.Address] cannot be null"
        assert flow.error(5).text() == "Property [address] of class [class org.davisononline.footy.core.Address] cannot be null"
        assert flow.error(6).text() == "Property [postCode] of class [class org.davisononline.footy.core.Address] cannot be null"

        // set some duffs
        personForm.givenName = "Dad"
        personForm.phone1 = "07000000000"
        personForm.email = "asd@asd"
        house.value("144")
        address.value("Some St.")
        postCode.value("XXX")
        flow.contButton.click()
        assert flow.errors.size() == 2
        assert flow.error(0).text() == "Property [email] of class [class org.davisononline.footy.core.Person] with value [asd@asd] is not a valid e-mail address"
        assert flow.error(1).text().startsWith("Property [postCode] of class [class org.davisononline.footy.core.Address] with value [XXX] does not match the required pattern")

        postCode.value("GU1 1DB")
        personForm.email = "asd@asd.com"
        flow.contButton.click()
        waitFor { at(TeamPage) }

        flow.contButton.click()
        waitFor { at(InvoicePage) }
        assert itemName(0) == "Joe Bloggs Junior"
        assert itemAmount(0).contains("1 x £60.00")
    }
}

