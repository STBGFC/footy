package org.davisononline.footy.tournament


/**
 * @author: darren
 */
class Competition implements Serializable {

    public static def GAME_FORMATS = ['5v5', '7v7', '9v9', '11v11']

    int teamLimit
    String name
    String gameFormat
    boolean open = true

    static belongsTo = [tournament: Tournament]

    static hasMany = [entered: Entry, waiting: Entry]

    static mappedBy = [entered: 'entered', waiting: 'waiting']

    static constraints = {
        teamLimit min: 2
        gameFormat inList: GAME_FORMATS
    }

    /**
     * sort by name
     *
     * @param other
     * @return
     */
    int compareTo(other) {
        if (!other) return 1
        return name?.compareTo(other.name)
    }

    /**
     * adds an entry to the main list if not full, or the waiting list
     * otherwise.
     *
     * @param entry
     * @return
     */
    def addEntry(Entry entry) {
        if (entered.size() < teamLimit) {
            this.addToEntered(entry)
        }
        else {
            this.addToWaiting(entry)
        }
        entry.competition = this
    }

    /**
     * returns true if the entry was on the waiting list and was
     * added to the main entry list.
     *
     * @param entry
     * @return
     */
    def promoteFromWaitingList(Entry entry) {
        if (this.waiting.contains(entry)) {
            this.removeFromWaiting(entry)
            this.addToEntered(entry)
            return true
        }
        return false
    }

    public String toString() {
        "$name ($gameFormat)"
    }

}
