package org.davisononline.footy.core

import grails.test.*

class PersonTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstraints() {
        mockForConstraintsTests(Person, [getGood()])
        def p = getGood()
        assertFalse p.validate()
        assertEquals "unique", p.errors["email"]
        p.email = "unique@example.com"
        assertTrue p.validate()
        
        p.phone1 = ""
        assertFalse p.validate()
        assertEquals "blank", p.errors["phone1"]
        
        p.phone1 = "123"
        assertTrue p.validate()        
    }
    
    void testComparable() {
        def ss = new TreeSet()
        ss.add getGood()
        def p2 = getGood()
        p2.familyName = "Edwards"
        ss.add p2
        def p3 = getGood()
        p3.familyName = "Charles"
        ss.add p3
        assertEquals ss.last().familyName, "Edwards"
        assertEquals ss.first().familyName, "Charles"
    }
    
    void testToString() {
        def p = getGood()
        assertEquals "Darren Davison", p.toString()
        p.givenName = "Darren Fred"
        assertEquals "Darren Fred Davison", p.toString()
        p.knownAsName = "Joe"
        assertEquals "Joe Davison", p.toString()
    }
    
    void testSortedName() {
        assertEquals "Davison, Darren", getGood().sortedName()
    }
    
    void testSetName() {
        def p = new Person()
        
        // no error..
        p.setName null
        p.setName ""
        
        p.setName "foo"
        assertNull p.givenName
        assertEquals "foo", p.familyName
        
        p.setName "foo bar"
        assertEquals "foo", p.givenName
        assertEquals "bar", p.familyName
        
        p.setName "foo bar baz"
        assertEquals "foo bar", p.givenName
        assertEquals "baz", p.familyName
    }
    
    public static getGood() {
        new Person(
            familyName: "Davison", 
            givenName: "Darren", 
            phone1: "0123456789",
            email: "example@example.com"
        )
    }

    
}
