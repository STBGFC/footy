package org.davisononline.footy.core

import grails.test.*
import org.davisononline.footy.core.Address

class RegistrationControllerTests extends ControllerUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testPersonCommandConstraints() {
        mockForConstraintsTests(PersonCommand)
        def p = new PersonCommand(
                givenName: 'Fred',
                familyName: 'Bassett',
                email:"foo@bar.com",
                phone1: '0876544332',
                address: Address.parse("123 Some St., GU1 1DB")
        )
        assertTrue p.validate()

        // givenName and familyName constraints are tested above

        p.email = ''
        assertFalse p.validate()
        p.email = 'foo@bar.com'
        assertTrue p.validate()

        p.phone1 = ''
        assertFalse p.validate()
        p.phone1 = '01234566789'
        assertTrue p.validate()

    }

    void testToPerson() {
        def p = new PersonCommand(
                givenName: 'Fred',
                familyName: 'Bassett',
                email:"foo@bar.com",
                phone1: '0876544332',
                address: Address.parse("123 Some St., GU1 1DB")
        )
        def person = p.toPerson()
        assertEquals 'Fred', person.givenName
        assertEquals 'Bassett', person.familyName
        assertEquals 'foo@bar.com', person.email
    }
}
