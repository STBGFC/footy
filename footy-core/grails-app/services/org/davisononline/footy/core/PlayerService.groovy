package org.davisononline.footy.core

import org.springframework.validation.BeanPropertyBindingResult

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
            if (!player.person.errors) player.person.errors = new BeanPropertyBindingResult(player.person, "name")
            return (player.person.save() && player.save())

        } catch (Exception ex) {
            log.error(ex)
            return error()
        }
    }
}
