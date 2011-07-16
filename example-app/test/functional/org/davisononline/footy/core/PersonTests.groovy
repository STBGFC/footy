package org.davisononline.footy.core

import geb.Page
import org.davisononline.footy.*


class EditPersonPage extends Page {
    static at = { title.startsWith("Edit details for: ") }
}

class PersonTests extends AbstractTestHelper {

    def addPerson(gn, fn, email) {
        go ""
        waitFor { at(HomePage) }
        personList.click()
        newPersonButton.click()
        person.personForm.givenName = gn
        person.personForm.familyName = fn
        person.personForm.phone1 = "07000000000"
        person.house.value("144")
        person.address.value("Some St.")
        person.postCode.value("GU1 1DB")
        person.personForm.email = email
        crud.saveButton.click()
    }

    def doQuals(mgr, quals) {
        go ""
        waitFor { at(HomePage) }
        personList.click()
        $("a", text: mgr).click()
        waitFor { at(CreatePersonPage) }
        addQual.click()
        waitFor { qualForm.present }
        quals.each { q ->
            qual.value(q)
            qualSubmit.click()
            //waitFor { qualList(0).find("span").text().contains(qual) }
        }
    }

    def addTeam(name, age, mgr) {
        go ""
        waitFor { at(HomePage) }
        teamList.click()
        newTeamButton.click()
        teamForm.ageBand = age
        teamForm.name = name
        manager.value(mgr)
        crud.saveButton.click()
    }

    void testCreatePerson() {
    }

    void testQualifications() {
    }

    void testLoginForManager() {
        go ""
        waitFor { at(HomePage) }
        auth.login("sa", "admin")
        addPerson("Jack", "Manager", "manager2@examplefc.com")
        doQuals("Jack Manager", ["FA Level 1"])
        addTeam("Boys", 8, "Jack Manager")
        assert crud.flash.text() == "Team U8 Boys created"
        addPerson("John", "Manager", "manager1@examplefc.com")
        doQuals("John Manager", ["FA Level 1"])
        addTeam("Reds", 8, "John Manager")
        assert crud.flash.text() == "Team U8 Reds created"
        auth.logout()
    }

}

