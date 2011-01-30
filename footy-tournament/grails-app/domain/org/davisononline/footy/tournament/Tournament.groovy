package org.davisononline.footy.tournament


/**
 * describes a tournament that multiple teams can enter
 * to play.
 * 
 * @author darren
 */
class Tournament {

    String name
    Date startDate
    Date endDate
    
    static hasMany = [entries: Entry]
    
    static constraints = {
        endDate(validator: { v, t->
            v >= t.startDate
        })
    }
}
