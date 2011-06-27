package org.davisononline.footy

import grails.plugin.geb.GebTests

class AbstractTestHelper extends GebTests {
    
    String getBaseUrl() {
        "http://localhost:8080/example-app"
    }

}

