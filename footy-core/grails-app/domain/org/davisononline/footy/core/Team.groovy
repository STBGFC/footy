package org.davisononline.footy.core

/**
 * Team
 */
class Team implements Serializable {

    // a team can play in a different League to the one
    // their Club is affiliated with
    League league
    String division
    String name
    Person manager
    SortedSet coaches
    SortedSet players
    int ageBand = 8
    boolean girlsTeam = false
    
    static belongsTo = [club: Club]

    static constraints = {
        division(nullable: true)
        name(blank: false, size: 2..30, unique: ['ageBand','club'])
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
