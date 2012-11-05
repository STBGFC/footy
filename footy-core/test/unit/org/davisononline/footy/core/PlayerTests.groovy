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
        //until we restore the Person.MINOR_UNTIL, this won't work
        //assertTrue p.validate()
        
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
            doctorTelephone: '09092348208',
            medical: 'None'
        )
    }

    void testAgeCalculation() {
        def p = getGood()
        def d = new Date()
        def currYr = d.format('yyyy') as int
        def currMonth = d.month
        def birthYr = currYr - 10 - ((currMonth < 8) ? 0 : 1)
        p.dateOfBirth = new Date("$birthYr/08/31")
        assertEquals(12, p.getAgeAtNextCutoff())
        p.dateOfBirth = new Date("$birthYr/09/01")
        assertEquals(11, p.getAgeAtNextCutoff())
    }
}
