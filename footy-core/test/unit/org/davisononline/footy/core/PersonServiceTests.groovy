package org.davisononline.footy.core

import grails.test.*

class PersonServiceTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testNull() {
        def ps = new PersonService()
        assertFalse(ps.saveOrUpdate(null))
    }
}
