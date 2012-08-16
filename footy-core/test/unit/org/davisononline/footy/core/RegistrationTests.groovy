package org.davisononline.footy.core

import grails.test.*

class RegistrationTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCheckDate() {
        def r = new Registration()
        r.date = new Date('2012/05/15')
        assert r.expiresBefore(new Date('2012/05/16'))
    }
}
