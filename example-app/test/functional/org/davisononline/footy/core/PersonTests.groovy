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
        auth.login("sa", "admin")
        personList.click()
        newPersonButton.click()
        person.personForm.givenName = gn
        person.personForm.familyName = fn
        person.personForm.phone1 = "07000000000"
        person.house.value("144")
        person.address.value("Some St.")
        person.postCode.value("GU1 1DB")
        person.personForm.email = email
        crud.contButton.click()
    }

    def doQuals(mgr, quals) {
        go ""
        waitFor { at(HomePage) }
        auth.login("sa", "admin")
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
        auth.login("sa", "admin")
        teamList.click()
        newTeamButton.click()
        teamForm.ageBand = age
        teamForm.name = name
        manager.value(mgr)
        crud.contButton.click()
    }

    void testCreatePerson() {
        addPerson("Rob", "Roberts", "rob@examplefc.com")
        go ""
        waitFor { at(HomePage) }
        personList.click()
        $("a", text: "Rob Roberts").click()
        auth.logout()
    }

    void testLoginForManager() {
        addPerson("Jack", "Manager", "manager2@examplefc.com")
        doQuals("Jack Manager", ["FA Level 1"])
        addTeam("Boys", 8, "Jack Manager")
        assert crud.flash.text() == "Team U8 Boys created"
        addPerson("John", "Manager", "manager1@examplefc.com")
        doQuals("John Manager", ["FA Level 1", "Emergency Aid", "CRB"])
        addTeam("Reds", 8, "John Manager")
        assert crud.flash.text() == "Team U8 Reds created"
        auth.logout()
    }

    void testDuplicateEmailWhenCreating() {
        addPerson("Ralph", "TheGreat", "john.parent@examplefc.com")
        assert crud.errors.size() == 1
        assert crud.error(0).text() == "Property [email] of class [class org.davisononline.footy.core.Person] with value [john.parent@examplefc.com] must be unique"
        auth.logout()
    }
}

