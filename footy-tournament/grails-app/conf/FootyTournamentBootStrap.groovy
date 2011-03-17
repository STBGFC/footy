import org.davisononline.footy.core.SecRole

class FootyTournamentBootStrap {

    def springSecurityService

    def init = { servletContext ->
        // security roles
        SecRole.findByAuthority('ROLE_TOURNAMENT_ADMIN') ?: new SecRole(authority: 'ROLE_TOURNAMENT_ADMIN').save(failOnError: true)
    }
    def destroy = {
    }
}
