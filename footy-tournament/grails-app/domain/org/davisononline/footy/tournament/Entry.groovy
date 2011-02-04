package org.davisononline.footy.tournament

/**
 * @author darren
 */
class Entry implements Serializable {

    def payment
    def contact
    def teams = []
    boolean emailConfirmationSent = false 
    
    static belongsTo = [tournament: Tournament]
    
    static constraints = {
    }
}
