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
		def existing = new Team(name:"Reds", ageBand:8, manager:new Person())
		mockForConstraintsTests(Team, [existing])
		
		def t = new Team(name:'')
		assertFalse t.validate()
		assertEquals "blank", t.errors["name"]
		assertEquals "nullable", t.errors["manager"]

		t.manager = new Person()

		// too long
		t.name = "ppppppppppppppppppppppppppppppppppp"
		assertFalse t.validate()
		assertEquals "size", t.errors["name"]

		// too short
		t.name = "p"
		assertFalse t.validate()
		assertEquals "size", t.errors["name"]

        // not unique
        t.name = "Reds"
        t.ageBand = 8
        assertFalse t.validate()
        assertEquals "unique", t.errors["name"]
		
        // unique outside ageBand
        t.name = "Reds"
        t.ageBand = 9
        t.league = new League(name:"EBYFL")
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
		def reds = new Team(name:'Reds', ageBand: 8)
		assertEquals("U8 Reds", reds.toString())
		def ravens = new Team(name:'Ravens', ageBand: 13, girlsTeam: true)
		assertEquals("U13 Ravens (Girls)", ravens.toString())
    }
}
