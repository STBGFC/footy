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
        p.dateOfBirth = new Date(92, 1, 1)
        p.team = new Team(name:'foo', manager:PersonTests.getGood(), ageBand: 17)
        assertTrue p.validate()
        
        p.dateOfBirth = new Date(104, 1, 1)
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
            doctor: 'Dr. Doolittle',
            doctorTelephone: '09092348208'
        )
    }

    void testAgeCalculation() {
        def p = getGood()
        p.dateOfBirth = new Date(103, 0, 20)
        assertEquals(8, p.getAgeAtNextCutoff())
        p.dateOfBirth = new Date(102, 10, 20)
        assertEquals(8, p.getAgeAtNextCutoff())
        p.dateOfBirth = new Date(103, 10, 20)
        assertEquals(7, p.getAgeAtNextCutoff())
        p.dateOfBirth = new Date(103, 7, 31)
        assertEquals(8, p.getAgeAtNextCutoff())
        p.dateOfBirth = new Date(103, 8, 1)
        assertEquals(7, p.getAgeAtNextCutoff())
    }
}
