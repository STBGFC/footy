package org.davisononline.footy.tournament

import org.davisononline.footy.core.Person
import org.davisononline.footy.core.Team
import org.grails.paypal.Payment


/**
 * @author darren
 */
class Entry implements Serializable, Comparable {

    public static def STRENGTH_LIST = ["High", "Medium", "Low"]

    Payment payment
    Person contact
    String clubAndTeam
    String league
    String strength
    Date dateEntered = new Date()

    /**
     * sort by date entered
     *
     * @param other
     * @return
     */
    int compareTo(other) {
        if (!other) return 1
        return dateEntered?.compareTo(other.dateEntered)
    }

    static constraints = {
        league nullable: true
        payment nullable: true
        clubAndTeam nullable: false, blank: false, unique: true
        strength inList: STRENGTH_LIST
    }

    public String toString() {
        clubAndTeam
    }
}
