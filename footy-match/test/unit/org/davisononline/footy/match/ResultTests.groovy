package org.davisononline.footy.match

import grails.test.*

class ResultTests extends GrailsUnitTestCase {

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCtor() {
        def r = new Result(1,2,3,4)
        assert r.homeGoalsHalfTime == 1
        assert r.awayGoalsHalfTime == 2
        assert r.homeGoalsFullTime == 3
        assert r.awayGoalsFullTime == 4
        assert r.toString() == "3-4"
    }

    void testToString() {
        def r = new Result(0,0,2,1)
        assert r.toString() == '2-1'
        r.awayGoalsFullTime = 3
        assert r.toString() == '2-3'
        r.awayGoalsExtraTime = 4
        r.homeGoalsExtraTime = 4
        assert r.toString() == '2-3'
        r.extraTime = true
        assert r.toString() == '4-4 (aet)'
        r.homeGoalsPenalties = 6
        r.awayGoalsPenalties = 7
        r.penalties = true
        assert r.toString() == '4-4 (aet) (6-7 after pens.)'
    }
}
