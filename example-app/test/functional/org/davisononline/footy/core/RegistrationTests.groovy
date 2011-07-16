package org.davisononline.footy.core

import geb.Browser
import geb.Page
import org.davisononline.footy.*

class TierPage extends Page {
    static at = { title == "Registration Type" }
    static content = {
        regForm { $("form#registration") }
        flow { module FlowModule }
    }
}

class PlayerPage extends Page {
    static at = { title.endsWith("Registration") }
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
        anotherParent { $("input", value:"Add another parent...") }
        person { module PersonFormModule, formName: 'registration' }
        flow { module FlowModule }
        crud { module CrudModule }
        auth { module AuthModule }
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

    def doPlayer(gn, fn, reg="Junior") {
        go "registration"
        waitFor { at(TierPage) }
        regForm.regTierId = reg
        flow.contButton.click()
        waitFor { at(PlayerPage) }
        playerForm.medical = "A bit deaf"
        givenName.value(gn)
        familyName.value(fn)
    }

    def doParent(email) {
        person.personForm.givenName = "Dad"
        person.personForm.phone1 = "07000000000"
        person.house.value("144")
        person.address.value("Some St.")
        person.postCode.value("GU1 1DB")
        person.personForm.email = email
    }

    def doFullReg(gn, fn, email, reg="Junior") {
        doPlayer(gn, fn, reg)
        flow.contButton.click()
        waitFor { at(PersonPage) }
        doParent(email)
        flow.contButton.click()
        waitFor { at(TeamPage) }
        flow.contButton.click()
        waitFor { at(InvoicePage) }
    }

    void testAdminLists() {
        doFullReg("Jody", "Bloggs", "abc123@bloggs.com")
        go "" //home
        waitFor { at(HomePage) }
        auth.login("sa", "admin")
        go "player/list"
        $("a", value:"Jody Bloggs").click()

        // invoice
        go ""
        waitFor { at(HomePage) }
        invoices.click()
        
    }
    
    void testDuplicateRegistration() {
        doFullReg("Fred", "Bloggs", "asd3@asd.com")
        // dupe..
        doPlayer("Fred", "Bloggs")
        flow.contButton.click()
        waitFor { at(InvoicePage) }
    }

    void testDuplicateEmail() {
        doFullReg("Julie", "Bloggs", "asd4@asd.com")
        // dupe email..
        doPlayer("Jackie", "Bloggs")
        flow.contButton.click()
        waitFor { at(PersonPage) }
        doParent("asd4@asd.com")
        flow.contButton.click()
        waitFor { at(PersonPage) }
        assert flow.errors.size() == 1
        assert flow.error(0).text() == "Property [email] of class [class org.davisononline.footy.core.Person] with value [asd4@asd.com] must be unique"
    }

    void testSecondParent() {
        doPlayer("Joanne", "Bloggs")
        flow.contButton.click()
        waitFor { at(PersonPage) }
        doParent("asd@asd.com")
        anotherParent.click()
        waitFor { at(PersonPage) }
        doParent("asd@asd.com")
        
        // should fail - dupe email even tho first not in DB yet
        flow.contButton.click()
        waitFor { at(PersonPage) }
        assert flow.errors.size() == 1
        assert flow.error(0).text() == "Cannot use the same email for both parents"

        person.personForm.email = "asd2@asd.com"
        flow.contButton.click()
        waitFor { at(TeamPage) }
    }

    void testRegistrationValidation() {
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
        doPlayer("Joe", "Bloggs")
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
        doParent("asd9@asd")
        person.postCode.value("XXX")
        flow.contButton.click()
        assert flow.errors.size() == 2
        assert flow.error(0).text() == "Property [email] of class [class org.davisononline.footy.core.Person] with value [asd9@asd] is not a valid e-mail address"
        assert flow.error(1).text().startsWith("Property [postCode] of class [class org.davisononline.footy.core.Address] with value [XXX] does not match the required pattern")

        person.postCode.value("GU1 1DB")
        person.personForm.email = "asd9@asd.com"
        flow.contButton.click()
        waitFor { at(TeamPage) }

        flow.contButton.click()
        waitFor { at(InvoicePage) }
        assert itemName(0) == "Joe Bloggs Junior"
        assert itemAmount(0).contains("1 x £60.00")
    }

    void testSeniorRegistration() {
        doFullReg("Alf", "Alpha", "zak@alpha.com", "Senior")
        assert itemName(0) == "Alf Alpha Senior"
        assert itemAmount(0).contains("1 x £80.00")
    }
    
}

