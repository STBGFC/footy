package org.davisononline.footy.tournament

import org.davisononline.footy.core.Person
import org.davisononline.footy.core.Team
import org.grails.paypal.Payment


/**
 * @author darren
 */
class Entry implements Serializable, Comparable {

    Payment payment
    Person contact
    boolean emailConfirmationSent = false 
    
    static belongsTo = [tournament: Tournament]
    
    static hasMany = [teams: Team]
    
    static constraints = {
        payment(nullable: true)
    }

    static mapping = {
        payment cascade: "all,delete-orphan"
    }

    /**
     * sort based on the name of the person registering
     *
     * @param other
     * @return
     */
    int compareTo(other) {
        if (!other) return 1
        return contact?.compareTo(other.contact)
    }
}
