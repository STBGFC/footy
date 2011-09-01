package org.davisononline.footy.match

import org.davisononline.footy.core.Team
import org.davisononline.footy.core.Person

/**
 * represents a game between two <tt>Team</tt>s and records the
 * type of game, the date, the <tt>Result</tt> (if any) and other
 * data.
 */
class Fixture {

    public static final String LEAGUE_GAME = 'League'
    public static final String CUP_GAME = 'Cup'
    public static final String FRIENDLY_GAME = 'Friendly'
    static def GAME_TYPES = [LEAGUE_GAME, CUP_GAME, FRIENDLY_GAME]

    Team homeTeam
    Team awayTeam
    Date dateTime
    String type = LEAGUE_GAME
    Result result
    Person referee

    boolean postponed = false


    static hasMany = [resources: MatchResource]

    static constraints = {
        dateTime nullable: true
        result nullable: true
        referee nullable: true
        type inList: GAME_TYPES
    }

    public String toString() {
        def mid = (result ? result : "vs.")
        "${homeTeam.club} ${homeTeam} ${mid} ${awayTeam.club} ${awayTeam}"
    }
}
