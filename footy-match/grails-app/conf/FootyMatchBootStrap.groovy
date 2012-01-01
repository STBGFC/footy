import org.davisononline.footy.core.SecRole
import org.davisononline.footy.core.SecUser
import org.davisononline.footy.core.SecUserSecRole

class FootyMatchBootStrap {

    def springSecurityService

    def init = { servletContext ->
        // security roles
        def fixSecRole = SecRole.findByAuthority('ROLE_FIXTURE_ADMIN') ?: new SecRole(authority: 'ROLE_FIXTURE_ADMIN').save(failOnError: true)
        def adminUser = SecUser.findByUsername('sa')
        if (adminUser && !adminUser.authorities.contains(fixSecRole)) {
            SecUserSecRole.create adminUser, fixSecRole
        }
    }
    def destroy = {
    }
}
