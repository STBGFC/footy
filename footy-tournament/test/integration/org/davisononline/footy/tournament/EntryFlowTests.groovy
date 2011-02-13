package org.davisononline.footy.tournament

import org.davisononline.footy.core.*

class ShoppingCartFlowTests extends grails.test.WebFlowTestCase {
    
    def getFlow() { 
        return new EntryController().applyFlow 
    }
    
    void testShoppingCartFlow() {
        startFlow() // void method
        assertCurrentStateEquals "setup"
    }
}

