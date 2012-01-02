package org.davisononline.footy.core

import org.davisononline.footy.*


class EditPersonPage extends FootyPage {
    static at = { title.startsWith("Edit details for: ") }
}

class PersonTests extends AbstractTestHelper {

    def addPerson(gn, fn, email) {
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
        teamList.click()
        newTeamButton.click()
        teamForm.ageBand = age
        teamForm.name = name
        manager.value(mgr)
        crud.contButton.click()
    }

    void testCreatePerson() {
        login("sa", "admin")
        addPerson("Rob", "Roberts", "rob@examplefc.com")
        auth.profilePage()
        personList.click()
        $("a", text: "Rob Roberts").click()
        auth.logout()
    }

    void testLoginForManager() {
        login("sa", "admin")
        // parents created in app bootstrap
        doQuals("John Parent", ["FA Level 1"])
        auth.profilePage()
        addTeam("Boys", 8, "John Parent")
        auth.profilePage()
        doQuals("Jules Parent", ["FA Level 1", "Emergency Aid", "CRB"])
        auth.profilePage()
        addTeam("Reds", 8, "Jules Parent")
        auth.logout()
        auth.login("Manager1", "Manager1")
        waitFor { at(ProfilePage) }
        $('a', text: 'U8 Boys').click()
        $("a", text: "Add/Change Sponsor").click()
        $('a', text: 'U8 Reds').click()
        assert $("a", text: "Add/Change Sponsor").size() == 0
        auth.logout()
        auth.login("manager2", "manager2")
        waitFor { at(ProfilePage) }
        $('a', text: 'U8 Reds').click()
        $("a", text: "Add/Change Sponsor").click()
        $('a', text: 'U8 Boys').click()
        assert $("a", text: "Add/Change Sponsor").size() == 0
        auth.logout()
    }

    void testDuplicateEmailWhenCreating() {
        login("sa", "admin")
        addPerson("Ralph", "TheGreat", "john.parent@examplefc.com")
        assert crud.errors.size() == 1
        assert crud.error(0).text() == "Property [email] of class [class org.davisononline.footy.core.Person] with value [john.parent@examplefc.com] must be unique"
        auth.logout()
    }
}

