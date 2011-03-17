import org.davisononline.footy.core.SecRole

class FootyTournamentBootStrap {

    def springSecurityService

    def init = { servletContext ->
        // security roles
        SecRole.findByAuthority('ROLE_TOURNAMENT') ?: new SecRole(authority: 'ROLE_TOURNAMENT').save(failOnError: true)
    }
    def destroy = {
    }
}
