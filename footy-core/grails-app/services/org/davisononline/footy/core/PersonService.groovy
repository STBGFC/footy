package org.davisononline.footy.core


/**
 * PersonService.. transactional/ACL methods for Person/Player
 * 
 * @author Darren Davison
 */
class PersonService {

    static transactional = true
    
    
    /**
     * checks a person/player for errors, then attempts to save a 
     * potentially transient address field first, finally saving
     * the person object.  
     * 
     * @param person
     * @return true if success, false otherwise
     */
    def saveOrUpdate(person) {
        if (person.hasErrors() || !person.validate()) {
            return false
        }
        log.debug("checking address")
        if (person.address != null  && !person.address.save(flush:true)) {
            return false
        }
        return person?.save(flush:true)
    }
    
    /**
     * @param player
     * @param guardian
     * @return
     */
    def save(player, guardian) {        
        player.guardian = guardian
        saveOrUpdate(guardian)
        player.save(flush: true)
    }
    
    /**
     * update the notes for a player with id supplied
     * 
     * @param personId
     * @param notes
     * @return
     */
    def updateNotes(personId, notes) {
        def p = getPlayer(personId)
        if (p) {
            p.notes = notes
            p.save(flush: true)
        }
    }
}
