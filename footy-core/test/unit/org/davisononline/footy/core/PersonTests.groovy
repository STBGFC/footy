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

        // check only one phone allowed to be null
        p.phone1 = null
        p.phone2 = null
        assertFalse p.validate()

        p.phone1 = "123"
        assertTrue p.validate()

        p.phone1 = null
        p.phone2 = "213"
        assertTrue p.validate()
    }

    void testBestPhone() {
        def p = getGood()
        p.phone1 = 'short'
        assert 'short' == p.bestPhone()

        p.phone1 = null
        p.phone2 = '22'
        assert '22' == p.bestPhone()
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
        p.givenName = null
        p.knownAsName = null
        assertEquals "Davison", p.toString()
    }
    
    void testSortedName() {
        assertEquals "Davison, Darren", getGood().sortedName()
    }
    
    void testSetName() {
        mockDomain(Person)
        mockForConstraintsTests(Person)
        def p = new Person()
        
        // no error..
        p.setFullName null
        p.setFullName ""
        
        p.setFullName "foo"
        p.phone1 = "029409"
        assertNull p.givenName
        assertEquals "foo", p.familyName
        assertFalse p.validate()
        
        p.setFullName "foo bar"
        assertEquals "foo", p.givenName
        assertEquals "bar", p.familyName
        assertTrue p.validate()
        
        p.setFullName "foo bar baz"
        assertEquals "foo bar", p.givenName
        assertEquals "baz", p.familyName
        assertTrue p.validate()
        
        // property access
        p.fullName = "foo bar"
        assertEquals "foo bar", p.fullName
        assertEquals "foo", p.givenName
        assertEquals "bar", p.familyName
        assertTrue p.validate()
        
    }

    void testConfig() {
        assertEquals 20, Person.MINOR_UNTIL
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
