package org.davisononline.footy.core

/**
 * Team
 */
class Team implements Serializable {

    static searchable = true

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
        ageBand(inList: [6,7,8,9,10,11,12,13,14,15,16,17,18,35])
    }
    
    def beforeUpdate() {
        checkAges()
    }
    def beforeInsert() {
        checkAges()
    }
    
    private checkAges() {
        if (ageBand < 11) girlsTeam = false
        if (vetsTeam) ageBand = 35
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
            otherTeam.vetsTeam == vetsTeam &&
            otherTeam.club == club)   
    }
    
    
}
