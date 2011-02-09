package org.davisononline.footy.tournament

import org.davisononline.footy.core.Person
import org.davisononline.footy.core.Team
import org.grails.paypal.Payment


/**
 * @author darren
 */
class Entry implements Serializable {

    Payment payment
    Person contact
    boolean emailConfirmationSent = false 
    
    static belongsTo = [tournament: Tournament]
    
    static hasMany = [teams: Team]
    
    static constraints = {
        payment(nullable: true)
    }
}
