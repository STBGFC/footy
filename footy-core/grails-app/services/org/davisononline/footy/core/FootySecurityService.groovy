package org.davisononline.footy.core

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

/**
 * Local enhancements to security services in order to avoid having to
 * implement a complete ACL system.
 */
class FootySecurityService {

    static transactional = true

    def springSecurityService


    /**
     * checks if the current security principal can be treated as the manager
     * of a specified team.  This generally means someone who IS the
     * manager, or who has access of ROLE_CLUB_ADMIN or higher.
     *
     * @param team
     * @return true if the principal can act as manager or false if not
     */
    def isAuthorisedForManager(teamId) {
        Team team = Team.get(teamId)
        return checkParam(team, {t, principal ->
            principal == t?.manager ||
            principal == t?.ageGroup?.coordinator
        })
    }

    /**
     * checks if the current security principal should be allowed to edit the
     * record of the specified player
     *
     * @param player
     * @return true if the principal can act as manager or false if not
     */
    def canEditPlayer(playerId) {
        Player player = Player.get(playerId)
        return checkParam(player, {p, principal ->
            principal == p?.team?.manager ||
            principal == p?.team?.ageGroup?.coordinator
        })
    }

    /**
     *
     * checks if the current security principal should be allowed to edit the
     * record of the specified person
     *
     * @param person
     * @return true if the principal can act as manager or false if not
     */
    def canEditPerson(personId) {

        Person person = Person.get(personId)

        /*
         * the closure will obtain all teams that the person / person's
         * kids play for.  If the principal is the manager of any of
         * those teams, the controller call will be allowed
         */
        return checkParam(person, {p, principal ->
            if (p == principal)
                return true
            def all = Player.findByPerson(p) ?: []
            all << Player.findAllByGuardianOrSecondGuardian(p, p)
            all = all.flatten()
            def teams = all.collect {it.team}
            (teams*.manager).contains(principal) || (teams*.ageGroup.coordinator).contains(principal)
        })
    }

    /*
     * performs basic checks to see if we can trivially permit or deny
     * access.  If not, executes a closure passed by the method caller,
     * passing in the subject (team/person/player) and the principal's
     * Person record as parameters.  The closure must return a boolean
     * indicating if the principal can edit the subject.
     */
    private checkParam(p, fn) {
        if (!p) return false

        def user = springSecurityService.currentUser
        if (!user) return false
        if (SpringSecurityUtils.ifAllGranted('ROLE_CLUB_ADMIN')) return true

        // up to the caller to decide
        return fn(p, Person.findByUser(user))
    }
}
