package org.davisononline.footy.core

import grails.test.*

class PlayerTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGuardianConstraint() {
        mockForConstraintsTests(Player)
        
        def p = getGood()
        assertTrue p.validate()

        p.guardian = null
        p.team = new Team(name:'foo', manager:PersonTests.getGood(), ageBand: 17)
        assertTrue p.validate()
        
        p.team.ageBand = 14
        assertFalse p.validate()
        assertEquals "validator", p.errors["guardian"]
        
        p.guardian = PersonTests.getGood()
        assertTrue p.validate()
    }
    
    void testOtherConstraints() {
        mockForConstraintsTests(Player)
        def p = getGood()
        assertTrue p.validate()
        
        p.person = null
        assertFalse p.validate()
        assertEquals "nullable", p.errors["person"]
        
        p = getGood()
        p.dateOfBirth = null
        assertFalse p.validate()
        assertEquals "nullable", p.errors["dateOfBirth"]
    }
    
    public static getGood() {
        new Player(
            person: PersonTests.getGood(),
            guardian: PersonTests.getGood(),
            dateOfBirth: new Date(),
        )
    }
}
