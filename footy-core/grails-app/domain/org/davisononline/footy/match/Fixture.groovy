package org.davisononline.footy.match

import org.davisononline.footy.core.Team
import org.davisononline.footy.core.Person

/**
 * represents a game between two <tt>Team</tt>s and records the
 * type of game, the date, the <tt>Result</tt> (if any) and other
 * data.
 */
class Fixture implements Comparable {

    public static final String LEAGUE_GAME = 'League'
    public static final String CUP_GAME = 'Cup'
    public static final String FRIENDLY_GAME = 'Friendly'
    static def GAME_TYPES = [LEAGUE_GAME, CUP_GAME, FRIENDLY_GAME]

    String guid = UUID.randomUUID().toString()
    Team team
    String opposition
    boolean homeGame = true
    Date dateTime
    String type = LEAGUE_GAME
    Person referee
    boolean adjustedKickOff = false
    boolean amendedResources = false

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

    static hasMany = [resources: MatchResource]

    // used to determine if the fixture secretary changed the KO time or adjusted resources
    static transients = ['adjustedKickOff', 'amendedResources']

    static constraints = {
        opposition blank: false, size: 3..70
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

    int compareTo(Object f) {
        if (! f?.dateTime) return -1
        return (f?.dateTime?.compareTo(dateTime))
    }
}
