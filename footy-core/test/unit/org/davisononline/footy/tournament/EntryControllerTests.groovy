package org.davisononline.footy.tournament

import org.davisononline.footy.core.Club;
import org.davisononline.footy.core.Person;

import grails.test.*

class EntryControllerTests extends ControllerUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }
        
    void testEntryFlow() {
        assert true
    }
    
    void testClubCommandConstraints() {
        mockForConstraintsTests(ClubCommand)
        def cc = new ClubCommand(
            name: "Foo",
            colours: "Red",
            clubSecretaryName: "Fred Bassett",
            clubSecretaryEmail: "fred@bassett.com",
            clubSecretaryAddress: "124 Some Rd",
            countyAffiliatedTo: "Berkshire",
            countyAffiliationNumber: "124234"
        )
        assertTrue cc.validate()
        
        // too short/long
        cc.name = "K"
        assertFalse cc.validate()
        cc.name = "ppppppppppssssssssssllllllllllppppppppppllllllllllsssss"
        assertFalse cc.validate()
        cc.name = "foo"
        assertTrue cc.validate()
        
        cc.colours = "R"
        assertFalse cc.validate()
        cc.colours = "wwwwwwwwwwppppppppppddddddddddlllll"
        assertFalse cc.validate()
        cc.colours = "Red"
        assertTrue cc.validate()
        
        cc.clubSecretaryName = "sss"
        assertFalse cc.validate()
        cc.clubSecretaryName = "ppppppppppppppppppppddddddddddddddddddddooooooooooooooo"
        assertFalse cc.validate()
        cc.clubSecretaryName = "dddddddddd"
        assertTrue cc.validate()        
        
        // email
        cc.clubSecretaryEmail = ""
        assertFalse cc.validate()
        cc.clubSecretaryEmail = "fffbbb"
        assertFalse cc.validate()
        cc.clubSecretaryEmail = "foo@bar.co.uk"
        assertTrue cc.validate()
    }

    void testRegCommandConstraints() {
        mockForConstraintsTests(RegisterCommand)
        def rc = new RegisterCommand(teamIds:[1,2,3])
        assertTrue rc.validate()
        
        rc.teamIds = []
        assertFalse rc.validate()
        assertEquals "minSize", rc.errors["teamIds"]
    }
}
