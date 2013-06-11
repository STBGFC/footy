import org.davisononline.footy.core.SecRole
import org.davisononline.footy.core.SecUser
import org.davisononline.footy.core.SecUserSecRole

class FootyTournamentBootStrap {

    def springSecurityService

    def init = { servletContext ->
        // security roles
        def tournamentRole = SecRole.findByAuthority('ROLE_TOURNAMENT_ADMIN') ?: new SecRole(authority: 'ROLE_TOURNAMENT_ADMIN').save(failOnError: true)
        def adminUser = SecUser.findByUsername('sa')
        if (adminUser && !adminUser.authorities.contains(tournamentRole)) {
            SecUserSecRole.create adminUser, tournamentRole
        }
    }
    def destroy = {
    }
}
