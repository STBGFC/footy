package org.davisononline.footy.match

/**
 * records the scores from a game played and offers various convenience
 * methods for grokking the outcome
 */
class Result {

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

    static constraints = {
    }

    /**
     * convenient constructor for the most common case
     */
    Result(hht, aht, hft, aft) {
        homeGoalsHalfTime = hht
        awayGoalsHalfTime = aht
        homeGoalsFullTime = hft
        awayGoalsFullTime = aft
    }

    /**
     * returns a familiar (for European sports) short result format
     */
    public String toString() {
        if (!extraTime)
            "${homeGoalsFullTime}-${awayGoalsFullTime}"
        else {
            def res = "${homeGoalsExtraTime}-${awayGoalsExtraTime} (aet)"
            if (penalties)
                res += " (${homeGoalsPenalties}-${awayGoalsPenalties} after pens.)"
            res
        }
    }
}
