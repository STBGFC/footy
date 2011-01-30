package org.davisononline.footy.tournament

import grails.test.*

class TournamentTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testDateConstraints() {
        mockForConstraintsTests(Tournament)
        def t = new Tournament(
            name:'May Tourney', 
            startDate: Date.parse('yyyy/MM/dd', '2011/05/14'),
            endDate: Date.parse('yyyy/MM/dd', '2011/05/15')
            )
        assertTrue t.validate()
        
        t.endDate = Date.parse('yyyy/MM/dd', '2011/05/14')
        assertTrue t.validate()
        
        t.endDate = Date.parse('yyyy/MM/dd', '2011/05/13')
        assertFalse t.validate()
        assertEquals "validator", t.errors["endDate"]
    }
}
