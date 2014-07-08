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
        log.info "Deleting Player ${player}" + (removePerson ? " and associated Person" : "")
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
        return (player.person.save() && player.save())
    }
}
