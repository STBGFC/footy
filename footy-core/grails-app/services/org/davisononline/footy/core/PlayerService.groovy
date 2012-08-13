package org.davisononline.footy.core

/**
 * transactional service methods for Player functionality
 */
class PlayerService {

    static transactional = true

    /**
     * delete a player, ensuring that any siblings are updated and the person
     * record is removed if needed
     *
     * @param player
     * @param removePerson
     * @return
     */
    def deletePlayer(Player player, boolean removePerson) {
        Player.findAllBySibling(player).each { p->
            p.sibling = null
            p.save()
        }

        def person
        if (!removePerson) {
            person = player.person
        }

        player.delete(flush: true)

        if (!removePerson) {
            person.save(flush:true)
        }
    }

    /**
     * update the supplied player in the database
     * 
     * @param player
     * @return true if successful, false otherwise
     */
    def updatePlayer(Player player) {
        // This often throws an NPE in prod (only) which seems to be due to:
        // http://jira.grails.org/browse/GRAILS-7471
        try {
            return (player.person.save() && player.save())
        }
        catch (NullPointerException npe) {
            log.error "NPE saving player data"
            log.error "Player is [${player}]"
            log.error "Errors are [${player?.errors}]"
            return false
        }
    }
}
