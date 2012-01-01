package org.davisononline.footy

import geb.junit4.GebReportingTest

class AbstractTestHelper extends GebReportingTest {
    
    def login(name, pwd) {
        go ''
        waitFor { at(HomePage) }
        auth.login(name, pwd)
        waitFor { at(ProfilePage) }
    }

    /*
    String getBaseUrl() {
        "http://localhost:8080/example-app"
    }
    */
}

