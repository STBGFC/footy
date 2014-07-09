package org.davisononline.footy.core

import grails.test.*

class AgeGroupTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstraints() {
        def existing = new AgeGroup(year:10, under:true)
        mockForConstraintsTests(AgeGroup, [existing])

        def a = new AgeGroup(year:10, under:true)
        assertFalse a.validate()
        assertEquals "unique", a.errors["year"]

        a.under = false
        assertTrue(a.validate())

        a.under = true
        assertFalse a.validate()
        assertEquals "unique", a.errors["year"]

        a.year = 9
        assertTrue(a.validate())
    }

    def testEquality() {

        def a1 = new AgeGroup(year:10, under:true)
        def a2 = new AgeGroup(year:10, under:true)
        def a3 = new AgeGroup(year:11, under:true)
        def a4 = new AgeGroup(year:10, under:false)

        assertTrue(a1 == a2)
        assertFalse(a1 == a3)
        assertFalse(a1.equals(a4))
    }

}
