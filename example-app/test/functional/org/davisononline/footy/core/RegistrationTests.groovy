package org.davisononline.footy.core

import org.davisononline.footy.FunctionalHelper
import org.junit.*

/**
 * test registration webflows and related functions
 */
class RegistrationTests extends FunctionalHelper {

    /**
     * index page
     */
    void testIndex() {
        get ('/')
        assertStatus 200
        assertContentContains "Welcome"
    }

    void doRegPage1(tier) {
        get ('/registration')
        assertStatus 200

        form ('registration') {
            selects['regTierId'].select tier
            click "_eventId_continue"
        }
        assertStatus 200
        assertContentContains "Enter details of the new player"
    }

    void doRegPage4() {
        form('registration') {
            click "_eventId_continue"
        }
        assertContentContains "Invoice is now due for payment"
    }

    /**
     * registration with all validation checks
     */
    void testRegistration() {
        
        doRegPage1("1")

        form('registration') {
            click "_eventId_submit"
        }
        assertStatus 200
        assertContentContains "Property [givenName] of class [class org.davisononline.footy.core.Person] cannot be blank"
        assertContentContains "Property [familyName] of class [class org.davisononline.footy.core.Person] cannot be blank"
        assertContentContains "Property [medical] of class [class org.davisononline.footy.core.Player] cannot be blank"

        form('registration') {
            selects["dateOfBirth_day"].select "10"
            selects["dateOfBirth_month"].select "10"
            selects["dateOfBirth_year"].select "2007"
            selects["dateJoinedClub_year"].select "2009"
            medical "None"
            person {
                givenName "Joe"
                familyName "Bloggs"
            }
            click "_eventId_submit"
        }
        assertStatus 200
        assertContentContains "Enter Parent/Guardian Details"

        form('registration') {
            click "_eventId_continue"
        }
        assertContentContains "Property [phone1] of class [class org.davisononline.footy.core.Person] with value [null] does not pass custom validation"
        assertContentContains "Property [phone2] of class [class org.davisononline.footy.core.Person] with value [null] does not pass custom validation"
        assertContentContains "Property [email] of class"
        assertContentContains "Property [house] of class"
        assertContentContains "Property [address] of class"
        assertContentContains "Property [postCode] of class"

        form('registration') {
            givenName "Dad"
            familyName "Bloggs"
            phone1 "072232323323"
            email "foo"
            address {
                house "1"
                address "Some St."
                town "Anytown"
                postCode "x"
            }
            click "_eventId_continue"
        }
        assertContentContains "[foo] is not a valid e-mail address"
        assertContentContains "[X] does not match the required pattern"

        form('registration') {
            email "foo@bar.com"
            address {
                postCode "GU1 1DB"
            }
            click "_eventId_continue"
        }
        assertContentContains "Assign Team"

        doRegPage4()

        login "sa", "admin" 
        doPlayerList()
        assertContentContains "Joe Bloggs"
        assertContentContains "Dad Bloggs"

        doPersonList()
        assertContentContains "John Secretary"
        assertContentContains "john.secretary@examplefc.com"
        assertContentContains "Dad Bloggs"
        assertContentContains "foo@bar.com"
    }

    /**
     * based on the previous registration (order is important!)
     * try to register a new parent with same email address
     */
    void testEmailDuplication() {

        doRegPage1("1")

        form('registration') {
            medical "None"
            person {
                givenName "Alf"
                familyName "Bloggs1"
            }
            click "_eventId_submit"
        }

        form('registration') {
            givenName "Dad"
            familyName "Bloggs1"
            phone1 "072232323323"
            email "foo@bloggs1.com"
            address {
                house "1"
                address "Some St."
                town "Anytown"
                postCode "GU1 1DB"
            }
            click "_eventId_continue"
        }

        doRegPage4()
        doRegPage1("1")

        form('registration') {
            medical "None"
            person {
                givenName "Alf"
                familyName "Bloggs2"
            }
            click "_eventId_submit"
        }

        form('registration') {
            givenName "Dad"
            familyName "Bloggs2"
            phone1 "072232323323"
            email "foo@bloggs1.com"
            address {
                house "1"
                address "Some St."
                town "Anytown"
                postCode "GU1 1DB"
            }
            click "_eventId_continue"
        }
        assertContentContains "Property [email] of class [class org.davisononline.footy.core.Person] with value [foo@bloggs1.com] must be unique"

        form('registration') {
            email "foo@bloggs2.com"
            click "_eventId_continue"
        }
        assertContentContains "Assign Team"
    }

    /**
     * based on initial registration, attempts to register child 
     * with same details again.. should get the first invoice 
     * page  instead.
     */
    void testDuplicateRegistration() {

        doRegPage1("1")

        form('registration') {
            selects["dateOfBirth_day"].select "10"
            selects["dateOfBirth_month"].select "10"
            selects["dateOfBirth_year"].select "2007"
            medical "None"
            person {
                givenName "Joe"
                familyName "Bloggs"
            }
            click "_eventId_submit"
        }
        assertStatus 200
        assertContentContains "Invoice is now due for payment"
    }

    /**
     * check bug that reverted YFJ back to current year upon
     * edit
     */
    void testYearFirstJoined() {
        def plist = Player.list()
        def p = plist[0]
        login "sa", "admin"
        get ("/player/edit/${p.id}")
        assertStatus 200
        form ("playerEditForm") {
            assert dateJoinedClub_year[0] == "2009"
        }
    }

}

