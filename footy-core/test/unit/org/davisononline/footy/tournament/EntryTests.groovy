package org.davisononline.footy.tournament

import grails.test.*

class EntryTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testEntryEqualsByClubAndName() {
        Competition c = new Competition(name: 'Foo', teamLimit: 2)
        def e = new Entry(clubAndTeam: "Foo", competition: c)
        def e2 = new Entry(clubAndTeam: "Foo", competition: c)
        assertTrue(e == e2)
    }
}
