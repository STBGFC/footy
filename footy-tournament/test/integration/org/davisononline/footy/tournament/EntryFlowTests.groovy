package org.davisononline.footy.tournament


import grails.test.*


/**
 * @author darren
 * @since 13/03/11
 */
class EntryFlowTests extends WebFlowTestCase {
    
    @Override
    Object getFlow() {
        new EntryController().applyFlow
    }

    @Override
    protected void setUp() {
        def t = new Tournament(
                name: 'TEST Tourney',
                startDate: new Date(),
                endDate: new Date(),
                openForEntry: true
        )
        t.save(flush:true)
        assert 1 == Tournament.count()
    }

    void testExampleFlow() {
        //mockRequest.parameters.put('id', '1')
        def viewSelection = startFlow()
        assert "setup" == flowExecution.activeSession.state.id
        assert true
    }
}
