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
    String clubAndTeam = ""
    String league
    String strength
    Date dateEntered = new Date()

    static belongsTo = [competition: Competition]
    Competition entered
    Competition waiting


    /*
     * don't allow entered and waiting to be null
     */
    static atLeastOneEntryValidator = { val, obj ->
        !( (obj.entered == null) && (obj.waiting == null) )
    }

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
        clubAndTeam nullable: false, blank: false, unique: ['competition']
        strength inList: STRENGTH_LIST
        entered validator: atLeastOneEntryValidator, nullable: true
        waiting validator: atLeastOneEntryValidator, nullable: true
    }

    public String toString() {
        clubAndTeam
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Entry entry = (Entry) o

        if (clubAndTeam != entry.clubAndTeam) return false

        return true
    }

    int hashCode() {
        return clubAndTeam ? clubAndTeam.hashCode() : 0
    }
}
