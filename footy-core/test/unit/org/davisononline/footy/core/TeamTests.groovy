package org.davisononline.footy.core

import grails.test.*

class TeamTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	void testConstraints() {
        def c = new Club(name:'FooClub')
		def existing = new Team(club: c, name:"Reds", ageBand:8, manager:new Person())
		mockForConstraintsTests(Team, [existing])
		
		def t = new Team(name:'', club: c)
		assertFalse t.validate()
		assertEquals "blank", t.errors["name"]
		assertEquals "nullable", t.errors["manager"]

		t.manager = new Person()
        t.league = new League(name:"EBYFL")

		// too long
		t.name = "ppppppppppppppppppppppppppppppppppp"
		assertFalse t.validate()
		assertEquals "size", t.errors["name"]

		// too short
		t.name = "p"
		assertFalse t.validate()
		assertEquals "size", t.errors["name"]

        // not unique, age band
        t.name = "Reds"
        t.ageBand = 8
        assertFalse t.validate()
        assertEquals "unique", t.errors["name"]
		
        // unique outside ageBand
        t.name = "Reds"
        t.ageBand = 9
        assertTrue t.validate()
        
        // unique only within club
        t.club = new Club(name:'BarClub')
        t.name = "Reds"
        t.ageBand = 8
        assertTrue t.validate()        
		
		// too young
		t.ageBand = 5
		assertFalse t.validate()
		assertEquals "min", t.errors["ageBand"]
		
		// too old
		t.ageBand = 19
		assertFalse t.validate()
		assertEquals "max", t.errors["ageBand"]
		
        t.ageBand = 10

		assertTrue t.validate()
		
	}

    void testToString() {
		def reds = new Team(name:'Reds', ageBand: 8, club: new Club(name:'STBGFC'))
		assertEquals("U8 Reds", reds.toString())
		def ravens = new Team(name:'Ravens', ageBand: 13, girlsTeam: true)
		assertEquals("U13 Ravens (Girls)", ravens.toString())
    }
}
