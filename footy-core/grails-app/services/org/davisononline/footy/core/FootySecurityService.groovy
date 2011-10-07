package org.davisononline.footy.core

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

/**
 * Local enhancements to security services in order to avoid having to
 * implement a complete ACL system.
 */
class FootySecurityService {

    def springSecurityService

    /**
     * checks if the current security principal can be treated as the manager
     * of a specified team.  This generally means someone who IS the
     * manager, or who has access of ROLE_CLUB_ADMIN or higher.
     *
     * @param team
     * @return true if the principal can act as manager or false if not
     */
    def isAuthorisedForManager(team) {

        if (!team) return false

        def user = springSecurityService.currentUser
        if (!user) return false

        if (SpringSecurityUtils.ifAllGranted('ROLE_CLUB_ADMIN')) return true

        return (Person.findByUser(user) == team?.manager)
    }
}
