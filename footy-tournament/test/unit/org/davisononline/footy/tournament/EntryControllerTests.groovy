package org.davisononline.footy.tournament

import org.davisononline.footy.core.Club;
import org.davisononline.footy.core.Person;

import grails.test.*

class EntryControllerTests extends ControllerUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testTeamCommandConstraints() {
        mockForConstraintsTests(TeamCommand)
        def tc = new TeamCommand(
            club: new Club(secretary: new Person(), name: "STBGFC"),
            ageBand: 8,
            teamName: "Reds",
            division: "C",
            contactName: "Joe Bloggs",
            email: "joe@bloggs.com",
            leagueId: 1
        )
        assertTrue tc.validate()
        
        tc.ageBand = 6
        assertFalse tc.validate()
        tc.ageBand = 8
        assertTrue tc.validate()
        tc.ageBand = 19
        assertFalse tc.validate()
        tc.ageBand = 8
        assertTrue tc.validate()
        
        tc.teamName = ''
        assertFalse tc.validate()
        tc.teamName = 'Reds'
        assertTrue tc.validate()
        
        tc.division = ''
        assertFalse tc.validate()
        tc.division = 'C'
        assertTrue tc.validate()
        
        tc.contactName = ''
        assertFalse tc.validate()
        tc.contactName = 'Joe Bloggs'
        assertTrue tc.validate()
        
        tc.email = 'blah'
        assertFalse tc.validate()
        tc.email = 'joe@bloggs.com'
        assertTrue tc.validate()
        
    }
}
