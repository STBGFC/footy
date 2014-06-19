package org.davisononline.footy.core

/**
 * Team
 */
class Team implements Serializable {

    static searchable = {
        spellCheck "include"
        name boost: 2.0
    }

    // a team can play in a different League to the one
    // their Club is affiliated with
    League league
    Division division
    String name
    Person manager
    Sponsor sponsor
    SortedSet coaches
    SortedSet players
    int ageBand = 8
    boolean girlsTeam = false
    boolean vetsTeam = false
    byte[] photo

    static belongsTo = [club: Club]
    static hasMany = [coaches: Person, players: Player, newsItems: NewsItem]

    static constraints = {
        division(nullable: true)
        name(blank: false, size: 2..30, unique: ['ageBand','club'])
        players(nullable: true)
		coaches(nullable: true)
        ageBand(inList: [6,7,8,9,10,11,12,13,14,15,16,17,18,21,35])
        photo(nullable: true)
        sponsor(nullable:true)
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
        return (
            otherTeam?.name == name &&
            otherTeam?.ageBand == ageBand &&
            otherTeam?.girlsTeam == girlsTeam &&
            otherTeam?.vetsTeam == vetsTeam &&
            otherTeam?.club == club
        )
    }

}
