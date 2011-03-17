import org.davisononline.footy.core.SecRole
import org.davisononline.footy.core.SecUser
import org.davisononline.footy.core.SecUserSecRole

class FootyCoreBootStrap {

    def springSecurityService

    def init = { servletContext ->
        // security roles and admin user
        def adminRole = SecRole.findByAuthority('ROLE_SYSADMIN') ?: new SecRole(authority: 'ROLE_SYSADMIN').save(failOnError: true)
        def clubAdminRole = SecRole.findByAuthority('ROLE_CLUB_ADMIN') ?: new SecRole(authority: 'ROLE_CLUB_ADMIN').save(failOnError: true)
        def managerRole = SecRole.findByAuthority('ROLE_COACH') ?: new SecRole(authority: 'ROLE_COACH').save(failOnError: true)

        def adminUser = SecUser.findByUsername('sa') ?: new SecUser(
            username: 'sa',
            password: springSecurityService.encodePassword('admin'),
            enabled: true
        ).save(failOnError: true)

        if (!adminUser.authorities.contains(adminRole)) {
            SecUserSecRole.create adminUser, adminRole
        }

    }
    def destroy = {
    }
}
