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
        login "sa", "admin"
        get '/person/create'
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
     * check person views
     */
    void testPersonList() {
        login "sa", "admin"
        get "/person/list"
        assertStatus 200
        assertContentContains "Jack Bloggs"
        assertContentContains "foo@baz.com"
        assertContentContains "0782323232323"
        assertContentContains "144 Some St., Anytown. GU1 1DB"
    }

    /**
     * edit person, add qualifications
     *
    void testQualifications() {
        testPersonList()
        click "Jack Bloggs"

        def id = byId("id").value
        get "/person/assignQualification/${id}"
        assertStatus 200

        post ("/person/addQualification") {
            headers['Content-Type'] = 'application/x-www-form-urlencoded'
            body {
                """
personId=${id}
type.id=1
attainedOn=date.struct
attainedOn_day=10
attainedOn_month=10
attainedOn_year=2007
"""
            }
        }
    }*/

    /**
     * delete the above created person
     */
    void testDeletePerson() {
        testPersonList()
        click "Jack Bloggs"
        click "_action_delete"

        assertStatus 200
        assertContentContains "Jack Bloggs deleted"
    }
    
}

