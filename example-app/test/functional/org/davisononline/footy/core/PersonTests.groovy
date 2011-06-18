package org.davisononline.footy.core

import org.davisononline.footy.FunctionalHelper


/**
 * test various person CRUD functions 
 */
class PersonTests extends FunctionalHelper {

    /**
     * create person form
     */
    void testCreatePerson() {
        login("sa", "admin")
        get ('/person/create')
        click "_action_save"

        assertContentContains "Property [givenName] of class"
        assertContentContains "Property [familyName] of class"
        assertContentContains "Property [phone1] of class"
        assertContentContains "Property [phone2] of class"
        assertContentContains "Property [house] of class [class org.davisononline.footy.core.Address]"
        assertContentContains "Property [address] of class [class org.davisononline.footy.core.Address]"
        assertContentContains "Property [postCode] of class [class org.davisononline.footy.core.Address]"

        form ('personEditForm') {
            givenName "Jack"
            familyName "Bloggs"
            phone1 "0782323232323"
            email "f"
            address {
                house "144"
                address "Some St."
                town "Anytown"
                postCode "XX"
            }
            click "_action_save"
        }

        assertContentContains "Property [email] of class [class org.davisononline.footy.core.Person] with value [f] is not a valid e-mail address"
        assertContentContains "Property [postCode] of class [class org.davisononline.footy.core.Address] with value [XX] does not match the required pattern"

        form ('personEditForm') {
            email "foo@baz.com"
            address {
                postCode "GU1 1db"
            }
            click "_action_save"
        }

        assertContentContains "Person Jack Bloggs created"
    }   

    /**
     * delete the above created person
     */
    void testDeletePerson() {
        def p = Person.findByGivenNameAndFamilyName("Jack", "Bloggs")
        login("sa", "admin")
        get ("/person/edit/${p.id}")
        click "_action_delete"

        assertStatus 200
        assertContentContains "Person ${p.id} deleted"
    }
}

