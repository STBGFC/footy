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
        def ag = new AgeGroup(year: 8)
		def existing = new Team(club: c, name:"Reds", ageGroup: ag, manager:new Person())
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
        t.ageGroup = ag
        assertFalse t.validate()
        assertEquals "unique", t.errors["name"]
		
        // unique outside ageBand
        t.name = "Reds"
        t.ageGroup = new AgeGroup(year: 9)
        assertTrue t.validate()
        
        // unique only within club
        t.club = new Club(name:'BarClub')
        t.name = "Reds"
        t.ageGroup = ag
        assertTrue t.validate()
	}
    
    void testGirlsTeamAgeBand() {
        mockDomain(Team)
        def t = new Team(
            league: new League(name:"EBYFL"),
            club: new Club(name:'FooClub'), 
            name:"Reds", 
            ageGroup: new AgeGroup(year: 8),
            manager:new Person()
            )
        assertTrue t.validate()
        t.girlsTeam = true
        t.save()
        assertFalse t.girlsTeam
        
        t.ageGroup = new AgeGroup(year: 14)
        t.girlsTeam = true
        t.save()
        assertTrue t.girlsTeam
        
    }

    void testToString() {
        def ag = new AgeGroup(year: 8)
        def ag13 = new AgeGroup(year: 13)
		def reds = new Team(name:'Reds', ageGroup: ag, club: new Club(name:'STBGFC'))
		assertEquals("U8 Reds", reds.toString())
		def ravens = new Team(name:'Ravens', ageGroup: ag13, girlsTeam: true)
		assertEquals("U13 Ravens (Girls)", ravens.toString())
    }
    
    /**
     * equals based on name, club, girlsTeam and age band
     */
    void testEquals() {
        def c = new Club(name:'STBGFC')
        def ag = new AgeGroup(year: 8)
        def reds = new Team(name:'Reds', ageGroup: ag, club: c)
        def reds2 = new Team(name:'Reds', ageGroup: ag, club: c)
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
        
        reds.ageGroup = new AgeGroup(year: 9)
        assert reds != reds2
        
        reds2.ageGroup = new AgeGroup(year: 9)
        assertEquals reds, reds2
        
        reds2.club = new Club(name: "Foo")
        assert reds != reds2
    }
}
