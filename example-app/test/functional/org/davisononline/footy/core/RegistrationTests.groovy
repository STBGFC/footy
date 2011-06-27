package org.davisononline.footy.core

import geb.Browser
import geb.Page
import geb.Module
import org.davisononline.footy.*

class ErrorModule extends Module {
    static content =  {
        errors { $("div.errors") }
    }
}

class TierPage extends Page {
    static at = { title == "Registration Type" }
    static content = {
        contButton { $("input[value=Continue]") }
    }
}

class PlayerPage extends Page {
    static at = { title == "Junior Registration" }
    static content = {
        contButton { $("input[value=Continue]") }
        errors { module ErrorModule }
    }
}

class RegistrationTests extends AbstractTestHelper {

    void testRegistration() {

        Browser.drive("http://localhost:8080/example-app/") {
            go "registration"

            // redirects to first page of flow.
            waitFor { at(TierPage) }
            assert at(TierPage)
            contButton.click()

            // check validations on player page
            waitFor { at(PlayerPage) }
            contButton.click()
            assert errors.size() == 3
            assert errors(0).text() == "Property [givenName] of class [class org.davisononline.footy.core.Person] cannot be blank"

        }
    }
}

