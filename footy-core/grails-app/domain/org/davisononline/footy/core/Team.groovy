package org.davisononline.footy.core

/**
 * Team
 */
class Team implements Serializable {

    String name
    Person manager
    SortedSet coaches
    SortedSet players
    int ageBand = 8
    boolean girlsTeam = false

    static constraints = {
        name(blank: false, size: 2..30, unique: 'ageBand')
        players(nullable: true)
		coaches(nullable: true)
        ageBand(min: 6, max:18)
    }
    
    def beforeValidate() {
        if (ageBand < 11) girlsTeam = false
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        "U${ageBand} ${name}" + (girlsTeam ? " (Girls)" : "")
    }
}
