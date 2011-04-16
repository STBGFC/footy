import org.davisononline.footy.core.SecRole
import org.davisononline.footy.core.SecUser
import org.davisononline.footy.core.SecUserSecRole
import org.davisononline.footy.core.QualificationType

class FootyCoreBootStrap {

    def springSecurityService

    def init = { servletContext ->
        // security roles and admin user
        def adminRole = SecRole.findByAuthority('ROLE_SYSADMIN') ?: new SecRole(authority: 'ROLE_SYSADMIN').save(failOnError: true)
        def clubAdminRole = SecRole.findByAuthority('ROLE_CLUB_ADMIN') ?: new SecRole(authority: 'ROLE_CLUB_ADMIN').save(failOnError: true)
        def coachRole = SecRole.findByAuthority('ROLE_COACH') ?: new SecRole(authority: 'ROLE_COACH').save(failOnError: true)

        def adminUser = SecUser.findByUsername('sa') ?: new SecUser(
            username: 'sa',
            password: springSecurityService.encodePassword('admin'),
            enabled: true
        ).save(failOnError: true)

        if (!adminUser.authorities.contains(adminRole)) {
            SecUserSecRole.create adminUser, adminRole
        }

        if (!adminUser.authorities.contains(clubAdminRole)) {
            SecUserSecRole.create adminUser, clubAdminRole
        }

        if (!adminUser.authorities.contains(coachRole)) {
            SecUserSecRole.create adminUser, coachRole
        }

        // qualifications
        if (QualificationType.count() == 0) {
            new QualificationType(name: "FA Level 1").save()
            new QualificationType(name: "FA Level 2").save()
            new QualificationType(name: "CRB", yearsValidFor: 3, category: QualificationType.OTHER).save()
            new QualificationType(name: "Emergency Aid", yearsValidFor: 3).save()
        }
    }
    def destroy = {
    }
}
