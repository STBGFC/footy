import org.davisononline.footy.core.AgeGroup
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
        def officerRole = SecRole.findByAuthority('ROLE_OFFICER') ?: new SecRole(authority: 'ROLE_OFFICER').save(failOnError: true)
        def coachRole = SecRole.findByAuthority('ROLE_COACH') ?: new SecRole(authority: 'ROLE_COACH').save(failOnError: true)
        def editorRole = SecRole.findByAuthority('ROLE_EDITOR') ?: new SecRole(authority: 'ROLE_EDITOR').save(failOnError: true)
        def fixSecRole = SecRole.findByAuthority('ROLE_FIXTURE_ADMIN') ?: new SecRole(authority: 'ROLE_FIXTURE_ADMIN').save(failOnError: true)
        def tournamentRole = SecRole.findByAuthority('ROLE_TOURNAMENT_ADMIN') ?: new SecRole(authority: 'ROLE_TOURNAMENT_ADMIN').save(failOnError: true)

        def adminUser = SecUser.findByUsername('sa') ?: new SecUser(
            username: 'sa',
            password: springSecurityService.encodePassword('admin'),
            enabled: true,
            passwordExpired: true
        ).save(failOnError: true)

        if (!adminUser.authorities.contains(adminRole)) {
            SecUserSecRole.create adminUser, adminRole
        }
        if (!adminUser.authorities.contains(officerRole)) {
            SecUserSecRole.create adminUser, officerRole
        }
        if (!adminUser.authorities.contains(clubAdminRole)) {
            SecUserSecRole.create adminUser, clubAdminRole
        }
        if (!adminUser.authorities.contains(coachRole)) {
            SecUserSecRole.create adminUser, coachRole
        }
        if (adminUser && !adminUser.authorities.contains(fixSecRole)) {
            SecUserSecRole.create adminUser, fixSecRole
        }
        if (!adminUser.authorities.contains(tournamentRole)) {
            SecUserSecRole.create adminUser, tournamentRole
        }

        // qualifications
        if (QualificationType.count() == 0) {
            new QualificationType(name: "FA Level 1").save()
            new QualificationType(name: "FA Level 2").save()
            new QualificationType(name: "CRB", yearsValidFor: 3, category: QualificationType.OTHER).save()
            new QualificationType(name: "Emergency Aid", yearsValidFor: 3).save()
        }

        // age groups
        if (AgeGroup.count() == 0) {
            12.times { age ->
                new AgeGroup(year: age + 7).save()
            }
        }
    }
    def destroy = {
    }
}
