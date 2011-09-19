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

    Team team
    String opposition
    boolean homeGame = true
    Date dateTime
    String type = LEAGUE_GAME
    Person referee

    static hasMany = [resources: MatchResource]

    // result
    boolean played = false
    boolean postponed = false
    int homeGoalsHalfTime = 0
    int awayGoalsHalfTime = 0
    int homeGoalsFullTime = 0
    int awayGoalsFullTime = 0

    // for cup / knock out competitions
    boolean extraTime = false
    int homeGoalsExtraTime = 0
    int awayGoalsExtraTime = 0

    boolean penalties = false
    int homeGoalsPenalties = 0
    int awayGoalsPenalties = 0

    String matchReport

    static constraints = {
        opposition blank: false, size: 10..70
        matchReport nullable: true
        dateTime nullable: true
        referee nullable: true
        type inList: GAME_TYPES
    }

    static mapping = {
        matchReport type: 'text'
    }

    /**
     * returns a familiar (for European sports) short result format
     */
    public String toString() {
        def mid = (postponed ? "P-P" : (played ? finalScore() : "vs."))
        def myteam = "${team.club} ${team}"
        def s = homeGame ? "${myteam} ${mid} ${opposition}" : "${opposition} ${mid} ${myteam}"
        if (penalties)
            s + " (${homeGoalsPenalties}-${awayGoalsPenalties} after pens)"
        else if (extraTime)
            s + " (aet)"
        else
            s
    }

    private finalScore() {
        if (!extraTime)
            "${homeGoalsFullTime} - ${awayGoalsFullTime}"
        else
            "${homeGoalsExtraTime} - ${awayGoalsExtraTime}"
    }
}
