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
		assertEquals "inList", t.errors["ageBand"]
		
		// too old
		t.ageBand = 19
		assertFalse t.validate()
		assertEquals "inList", t.errors["ageBand"]

		// vet
		t.ageBand = 35
		assertTrue t.validate()
		
        t.ageBand = 10
		assertTrue t.validate()
		
	}
    
    void testGirlsTeamAgeBand() {
        mockDomain(Team)
        def t = new Team(
            league: new League(name:"EBYFL"),
            club: new Club(name:'FooClub'), 
            name:"Reds", 
            ageBand:8, 
            manager:new Person()
            )
        assertTrue t.validate()
        t.girlsTeam = true
        t.save()
        assertFalse t.girlsTeam
        
        t.ageBand = 14
        t.girlsTeam = true
        t.save()
        assertTrue t.girlsTeam
        
    }

    void testVetsTeamAgeBand() {
        mockDomain(Team)
        def t = new Team(
            league: new League(name:"EBYFL"),
            club: new Club(name:'FooClub'),
            name:"Reds",
            ageBand:8,
            manager:new Person()
            )
        assertTrue t.validate()
        t.vetsTeam = true
        t.save()
        assertEquals(35, t.ageBand)        
    }

    void testToString() {
		def reds = new Team(name:'Reds', ageBand: 8, club: new Club(name:'STBGFC'))
		assertEquals("U8 Reds", reds.toString())
		def ravens = new Team(name:'Ravens', ageBand: 13, girlsTeam: true)
		assertEquals("U13 Ravens (Girls)", ravens.toString())
        def vets = new Team(name: "Dads United", ageBand: 8, vetsTeam: true)
		assertEquals("Dads United (Vets)", vets.toString())
        vets.girlsTeam = true
		assertEquals("Dads United (Girls, Vets)", vets.toString())
    }
    
    /**
     * equals based on name, club, girlsTeam and age band
     */
    void testEquals() {
        def c = new Club(name:'STBGFC')
        def reds = new Team(name:'Reds', ageBand: 8, club: c)
        def reds2 = new Team(name:'Reds', ageBand: 8, club: c)
        assertEquals reds, reds2
        
        reds.division = "B"
        reds2.division = "A"
        assertEquals reds, reds2
        
        reds.girlsTeam = false
        reds2.girlsTeam = true
        assert reds != reds2
        
        reds2.girlsTeam = false
        assertEquals reds, reds2
        
        reds.name = "Foo"
        assert reds != reds2
        
        reds2.name = "Foo"
        assertEquals reds, reds2
        
        reds.ageBand = 9
        assert reds != reds2
        
        reds2.ageBand = 9
        assertEquals reds, reds2
        
        reds2.club = new Club(name: "Foo")
        assert reds != reds2
    }
}
