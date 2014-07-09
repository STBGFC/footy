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
    AgeGroup ageGroup
    boolean girlsTeam = false
    byte[] photo

    static belongsTo = [club: Club]
    static hasMany = [coaches: Person, players: Player, newsItems: NewsItem]

    static constraints = {
        division(nullable: true)
        name(blank: false, size: 2..30, unique: ['ageGroup','club'])
        players(nullable: true)
		coaches(nullable: true)
        photo(nullable: true)
        sponsor(nullable:true)
    }
    
    def beforeUpdate() {
        checkAges()
    }

    def beforeInsert() {
        checkAges()
    }

    def getAgeBand() {
        ageGroup.year
    }
    
    private checkAges() {
        if (ageGroup.year < 11) girlsTeam = false
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        "${ageGroup} ${name}" + (girlsTeam ? " (Girls)" : "")
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
            otherTeam?.ageGroup == ageGroup &&
            otherTeam?.girlsTeam == girlsTeam &&
            otherTeam?.club == club
        )
    }

}
