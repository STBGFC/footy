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
    
    void testParseGood() {
        mockForConstraintsTests(Address)
        def a = Address.parse("123 st.\nTown\nGU1 1DB")
        assertEquals "123 st.", a.address
        
        a = Address.parse("123 st.\nVillage\nTown\nGU1 1DB")
        assertEquals "123 st., Village", a.address
        
        a = Address.parse("123 st.\nGU1 1DB")
        assertNull a.town
    }
    
    void testParseTooFewParts() {
        def a = Address.parse("xxxxxxx")
        assertNull a
    }
    
    void testParseBadPcode() {
        mockForConstraintsTests(Address)
        def a = Address.parse("xxxx\nXXXXXX")
        assertNotNull a
        assertFalse a.validate()
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

