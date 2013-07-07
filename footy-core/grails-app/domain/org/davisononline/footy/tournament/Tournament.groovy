package org.davisononline.footy.tournament


/**
 * describes a tournament that multiple teams can enter
 * to play.
 * 
 * @author darren
 */
class Tournament implements Serializable {

    String name
    Date startDate
    Date endDate
    boolean openForEntry = false
    double costPerTeam = 10.00
    
    static hasMany = [entries: Entry]
    
    static constraints = {
        endDate(validator: { v, t->
            v >= t.startDate
        })
    }
    
    def teamsEntered() {
        def results = []
        entries.each { e ->
            results << e.teams
        }
        results.flatten()
    }
}