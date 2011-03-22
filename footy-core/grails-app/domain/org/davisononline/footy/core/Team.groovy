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
    boolean vetsTeam = false
    
    static belongsTo = [club: Club]

    static constraints = {
        division(nullable: true)
        name(blank: false, size: 2..30, unique: ['ageBand','club'])
        players(nullable: true)
		coaches(nullable: true)
        ageBand(min: 6, max:18)
    }
    
    def beforeUpdate() {
        checkAgeOfGirlsTeam()
    }
    def beforeInsert() {
        checkAgeOfGirlsTeam()
    }
    
    private checkAgeOfGirlsTeam() {
        if (ageBand < 11) girlsTeam = false
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        if (!vetsTeam)
            "U${ageBand} ${name}" + (girlsTeam ? " (Girls)" : "")
        else
            "${name} (" + (girlsTeam ? "Girls, " : "") + "Vets)"
    }

    /**
     * mainly for use in collections and comparisons
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(otherTeam) {
        return (otherTeam?.name == name && 
            otherTeam.ageBand == ageBand &&
            otherTeam.girlsTeam == girlsTeam &&
            otherTeam.club == club)   
    }
    
    
}
