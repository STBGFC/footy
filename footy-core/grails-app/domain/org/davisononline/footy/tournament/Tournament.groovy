package org.davisononline.footy.tournament

import org.davisononline.footy.core.Person


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
    String cclist
    Person treasurer
    
    static hasMany = [competitions: Competition]
    
    static constraints = {
        endDate(validator: { v, t->
            v >= t.startDate
        })
        treasurer nullable: true
    }

    boolean hasEntries() {
        def signups = false
        competitions.each { c->
            if (c.entered.size() > 0 || c.waiting.size() > 0)
                signups = true
        }
        return signups
    }

    public String toString() {
        name
    }

}
