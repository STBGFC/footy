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
        def e = new Entry(clubAndTeam: "Foo")
        def e2 = new Entry(clubAndTeam: "Foo")
        assertTrue(e == e2)
    }
}
