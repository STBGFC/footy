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
        assertFalse a.validate()

        // blank house
        a = getGood()
        a.house = ""
        assertFalse a.validate()

        // null pcode
        a = getGood()
        a.postCode = null
        assertFalse a.validate()
    }
    
    void testUpperCasePostCode() {
        def a = getGood()
        assertEquals "GU1 1DB", a.postCode
        a.postCode = "ls11 5dd"
        assertEquals "LS11 5DD", a.postCode
    }

    void testToString() {
        def a = getGood()
        assertEquals "144 Foo Bar, Guildford. GU1 1DB", a.toString()
    }
    
    public static getGood() {
        new Address(house: "144", address: "Foo Bar", town: "Guildford", postCode: "GU1 1DB")
    }
}

