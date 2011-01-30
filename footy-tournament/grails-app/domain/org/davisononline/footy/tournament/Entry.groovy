package org.davisononline.footy.tournament

/**
 * @author darren
 */
class Entry {

    def payment
    def contact
    def teams = []
    
    static belongsTo = [tournament: Tournament]
    
    static constraints = {
    }
}
