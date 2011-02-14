package org.davisononline.footy.tournament

import grails.test.*

class TournamentControllerTests extends ControllerUnitTestCase {
    
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testIndex() {
        controller.index()
        assertEquals "list", controller.redirectArgs.action
    }
    
//    void testList() {
//        params.max = 120
//        controller.list()
//        
//    }
}
