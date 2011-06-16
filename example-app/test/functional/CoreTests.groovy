import functionaltestplugin.FunctionalTestCase

class CoreTests extends FunctionalTestCase {

    void testIndex() {
        get ('/')
        assertStatus 200
        assertContentContains "Welcome"
    }

    void testRegistration() {
        get ('/registration')
        assertStatus 200

        form ('registration') {
            selects['regTierId'].select "1"
            click "_eventId_continue"
        }
        assertStatus 200
        assertContentContains "Enter details of the new player"

        form('registration') {
            click "_eventId_submit"
        }
        assertStatus 200
        assertContentContains "Property [givenName] of class [class org.davisononline.footy.core.Person] cannot be blank"

        form('registration') {
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

        form('registration') {
            click "_eventId_continue"
        }
        assertContentContains "Invoice is now due for payment"
    }

}
        
