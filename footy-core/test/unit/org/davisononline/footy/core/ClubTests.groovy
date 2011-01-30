package org.davisononline.footy.core

import grails.test.*

class ClubTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstraints() {
        def existing = new Club(name:"STBGFC", secretary:new Person())
        mockForConstraintsTests(Club, [existing])
        
        def c = new Club(name:'foo')
        assertFalse c.validate()
        assertEquals "nullable", c.errors["secretary"]
        assertEquals "nullable", c.errors["colours"]

        c.secretary = new Person()

        // too long
        c.name = "ccccccccccccccccccccccccccccccccccccccccccccccccccccccc"
        assertFalse c.validate()
        assertEquals "size", c.errors["name"]

        // too short
        c.name = "c"
        assertFalse c.validate()
        assertEquals "size", c.errors["name"]

        // not unique
        c.name = "STBGFC"
        assertFalse c.validate()
        assertEquals "unique", c.errors["name"]

        // URL
        c.website = 'foo'
        assertFalse c.validate()
        assertEquals "url", c.errors["website"]
        
        c.website = "http://foo.bar.com"
        c.name = "Ascot"
        c.colours = "Blue and Yellow"
        assertTrue c.validate()
        
    }

}

