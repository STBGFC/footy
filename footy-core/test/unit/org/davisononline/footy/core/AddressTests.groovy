package org.davisononline.footy.core

import grails.test.*

class AddressTests extends GrailsUnitTestCase {
    
    void testConstraints() {
        mockForConstraintsTests(Address)
        def a = getGood()
        assertTrue a.validate()
        a.town = null
        assertTrue a.validate()
        
        // invalid postcode
        a = getGood()
        a.postCode = "XX1111XX"
        assertFalse a.validate()
        
        // invalid address (1st line)
        a = getGood()
        a.address = ""
        assertFalse a.validate()
        
        // blank postcode
        a = getGood()
        a.postCode = ""
        assertTrue a.validate()
    }
    
    void testParseOneLine() {
        mockForConstraintsTests(Address)
        def a = Address.parse("x")
        assertEquals "x", a.address
        assertTrue a.validate()
    }
    
    void testParseBlankLines() {
        mockForConstraintsTests(Address)
        def a = Address.parse("x\n\n\nGU1 1db\n\n\n")
        assertEquals "x, GU1 1db", a.address
        assertNull a.town
        assertEquals "GU1 1DB", a.postCode
        assertTrue a.validate()
    }
    
    void testParse3parts() {
        mockForConstraintsTests(Address)
        def a = Address.parse("123 st.\nTown\nGU1 1DB")
        assertEquals "123 st.", a.address
        assertTrue a.validate()
    }
    
    void testParse4parts() {
        mockForConstraintsTests(Address)
        def a = Address.parse("123 st.\nVillage\nTown\nGU1 1DB")
        assertEquals "123 st., Village", a.address
        assertTrue a.validate()
    }
    
    void testParse2parts() {
        mockForConstraintsTests(Address)
        def a = Address.parse("123 st.\nGU1 1DB")
        assertNull a.town
        assertTrue a.validate()
    }
    
    void testNullPostCodeForNPE() {
        def a = new Address()
        a.setPostCode(null)
        assertNull a.postCode // ie no NPE thrown
    }
    
    void testParseTooFewParts() {
        def a = Address.parse("")
        assertNull a
        a = Address.parse(null)
        assertNull a
    }
    
    void testParseBadPcode() {
        mockForConstraintsTests(Address)
        def a = Address.parse("xxxx\nXXXXXX")
        assertNotNull a
        assertEquals "xxxx\nXXXXXX", a.address
        assertTrue a.validate()
    }
    
    void testUpperCasePostCode() {
        def a = getGood()
        assertEquals "GU1 1DB", a.postCode
        a.postCode = "ls11 5dd"
        assertEquals "LS11 5DD", a.postCode
    }
    
    public static getGood() {
        new Address(address: "144 Foo Bar", town: "Guildford", postCode: "GU1 1DB")
    }
}

