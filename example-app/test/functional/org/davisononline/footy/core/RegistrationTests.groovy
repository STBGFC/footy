package org.davisononline.footy.core

import geb.Browser
import org.davisononline.footy.*
import com.dumbster.smtp.*
import org.junit.*


class TierPage extends FootyPage {
    static at = { title == "Registration Type" }
    static content = {
        regForm { $("form#registration") }
        flow { module CrudModule }
    }
}

class PlayerPage extends FootyPage {
    static at = { title.endsWith("Registration") }
    static content = {
        playerForm { $("form#registration") }
        givenName { playerForm.find("input", name:"person.givenName") }
        familyName { playerForm.find("input", name:"person.familyName") }
        flow { module CrudModule }
    }
}

class PersonPage extends FootyPage {
    static at = { title == "Enter Parent/Guardian Details" }
    static content = {
        anotherParent { $("input", value:"Add another parent...") }
        person { module PersonFormModule, formName: 'registration' }
        flow { module CrudModule }
    }
}

class TeamPage extends FootyPage {
    static at = { title == "Assign Team" }
    static content = {
        teamForm { $("form#registration") }
        flow { module CrudModule, label: "Finish and Pay..." }
    }
}

class DupePage extends FootyPage {
    static at = { title == "Already Registered" }
}

class InvoicePage extends FootyPage {
    static at = { title == "Payment" }
    static content = {
        lineItems { $("tr.invoiceBody") }
        lineItem { i -> lineItems[i] }
        itemName { n -> lineItem(n).find("td", 1).text() }
        itemAmount { n -> lineItem(n).find("td.amount").text() }
    }
}

class RegistrationTests extends AbstractTestHelper {

    def smtp

    @Before
    void setUp() {
        println "Starting smtp server.."
        smtp = SimpleSmtpServer.start(2525)
    }

    @After
    void tearDown() {
        smtp.stop()
    }

    def doPlayer(gn, fn, reg="Junior") {
        go "registration"
        waitFor { at(TierPage) }
        regForm.regTierId = reg
        flow.contButton.click()
        waitFor { at(PlayerPage) }
        playerForm.dateOfBirth_year = '2003'
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
        assert smtp.receivedEmailSize == 1
        smtp.receivedEmail.each { email ->
            assert email.body.contains("Dear Dad,")
            assert email.body.contains("Jody Bloggs")
        }
        login("sa", "admin")
        playerList.click()
        $("a", value:"Jody Bloggs").click()

        // invoice
        go "login/profile"
        waitFor { at(ProfilePage) }
        invoices.click()
        // TODO: check invoice values
        auth.logout()
    }
    
    void testDuplicateRegistration() {
        doFullReg("Fred", "Bloggs", "asd3@asd.com")
        // dupe..
        doPlayer("Fred", "Bloggs")
        flow.contButton.click()
        waitFor { at(DupePage) }
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
        assert flow.errors.size() == 4
        assert flow.error(0).text() == "Property [givenName] of class [class org.davisononline.footy.core.Person] cannot be blank"
        assert flow.error(1).text() == "Property [familyName] of class [class org.davisononline.footy.core.Person] cannot be blank"
        assert flow.error(2).text() == "Property [dateOfBirth] of class [class org.davisononline.footy.core.Player] cannot be null"
        assert flow.error(3).text() == "Property [medical] of class [class org.davisononline.footy.core.Player] cannot be blank"

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
        teamForm.leagueRegistrationNumber = '123456'

        flow.contButton.click()
        waitFor { at(InvoicePage) }
        assert itemName(0) == "Joe Bloggs Junior"
        assert itemAmount(0).contains("1 x £60.00")||itemAmount(0).contains("1 x GBP60.00")
        assert smtp.receivedEmailSize == 1

        // dupe reg number
        go "registration"
        waitFor { at(TierPage) }
        assert at(TierPage)
        flow.contButton.click()
        waitFor { at(PlayerPage) }
        doPlayer("Jock", "Bloggs")
        // can only seem to make the longhand version work to set this field..
        playerForm.find("select", name:"guardian.id").value("Dad Bloggs")
        flow.contButton.click()
        waitFor { at(TeamPage) }
        teamForm.leagueRegistrationNumber = '123456'
        flow.contButton.click()
        waitFor { at(TeamPage) }
        assert flow.errors.size() == 1
        assert flow.error(0).text() == "Property [leagueRegistrationNumber] of class [class org.davisononline.footy.core.Player] with value [123456] must be unique"

        teamForm.leagueRegistrationNumber = '123458'
        flow.contButton.click()
        waitFor { at(InvoicePage) }
        assert smtp.receivedEmailSize == 2
    }

    void testSeniorRegistration() {
        doFullReg("Alf", "Alpha", "zak@alpha.com", "Senior")
        assert itemName(0) == "Alf Alpha Senior"
        assert itemAmount(0).contains("1 x £80.00")||itemAmount(0).contains("1 x GBP80.00")
        assert smtp.receivedEmailSize == 1
    }
    
}

