package org.davisononline.footy.tournament

import grails.test.*
import org.davisononline.footy.core.*

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
    
    void teamsEnteredTests() {
        def t = new Tournament(
            name:'May Tourney', 
            startDate: Date.parse('yyyy/MM/dd', '2011/05/14'),
            endDate: Date.parse('yyyy/MM/dd', '2011/05/15')
            )
        def c1 = new Person(givenName: 'Fred', familyName: 'Bloggs')
        def e1 = new Entry(contact: c1)
        def t1 = new Team(name:'Reds', ageBand:8, manager: c1)
        e1.addToTeams(t1)
        
        def c2 = new Person(givenName: 'Gary', familyName: 'Player')
        def e2 = new Entry(contact: c2)
        def t2 = new Team(name:'Blues', ageBand:8, manager: c2)
        e1.addToTeams(t2)
        
        def c3 = new Person(givenName: 'Harry', familyName: 'Adam')
        def e3 = new Entry(contact: c3)
        def t3 = new Team(name:'Greens', ageBand:8, manager: c3)
        e1.addToTeams(t3)
        
        t.addToEntries(e1)
        t.addToEntries(e2)
        t.addToEntries(e3)
        
        assertEquals t.teamsEntered, [t1,t3,t2]
    }
}
