package org.davisononline.footy.match

import org.davisononline.footy.core.Person

class RefereeReport {

    Fixture fixture

    // could be different from the one assigned to the fixture
    Person referee

    /*
     * not very flexible, but a full question/answer tableset seems overkill.
     * Questions are simply declared in the view for now
     */
    List scores

    String comments


    static constraints = {
        comments nullable: true
        referee nullable: true
    }

    static mapping = {
        comments type: 'text'
    }
    
    static hasMany = [scores: Integer]

    static transients = ['total']

    def getTotal() {
        // nulls shouldn't be possible if the input validation works, but
        // removing them guards against NPE at least..
        (scores - null).sum()
    }

    String toString() {
        "Ref Report for ${referee.toString()} (${fixture.toString()}) on ${fixture.dateTime.format('dd/MM/yy')}"
    }
}
