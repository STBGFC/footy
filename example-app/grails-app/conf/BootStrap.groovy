import org.davisononline.footy.core.*
import org.davisononline.footy.tournament.*

class BootStrap {

    def searchableService
    def springSecurityService

    def init = { servletContext ->
        if (League.count() == 0) {
            def l=new League(name:"North").save()
            def l2=new League(name:"South").save()
        }
        def clubSec
        def club
        if (!Club.homeClub) {
            clubSec = new Person(
                givenName: 'John',
                familyName: 'Secretary',
                phone1: '07000000000',
                email: 'john.secretary@examplefc.com',
                eligibleParent: true
            ).save(flush: true)
            club = new Club(
                name: 'Example FC',
                colours: 'Blue and White',
                secretary: clubSec
            ).save()

        }

        def p1 = new Person(
            givenName: 'John',
            familyName: 'Parent',
            phone1: '07000000001',
            email: 'john.parent@examplefc.com',
            eligibleParent: true
        ).save(flush: true)
        def p2 = new Person(
            givenName: 'Jules',
            familyName: 'Parent',
            phone1: '07000000002',
            email: 'jules.parent@examplefc.com',
            eligibleParent: true
        ).save(flush: true)

        if (RegistrationTier.count() == 0) {
            new RegistrationTier(
                    name: "Junior",
                    amount: 60.00,
                    siblingDiscount: 15.00
            ).save()
            new RegistrationTier(
                    name: "Senior",
                    amount: 80.00,
                    siblingDiscount: 15.00
            ).save()
        }        
        
        def sa = SecUser.findByUsername('sa')
        sa.passwordExpired = false
        sa.save(failOnError: true)

        // set up a couple of logins that can be used in tests
        def manager1 = SecUser.findByUsername('manager1') ?: new SecUser(
            username: 'manager1',
            password: springSecurityService.encodePassword('manager1'),
            enabled: true
        ).save(failOnError: true)
        def manager2 = SecUser.findByUsername('manager2') ?: new SecUser(
            username: 'manager2',
            password: springSecurityService.encodePassword('manager2'),
            enabled: true
        ).save(failOnError: true)
        def clubAdmin = SecUser.findByUsername('clubAdmin') ?: new SecUser(
            username: 'clubAdmin',
            password: springSecurityService.encodePassword('clubAdmin'),
            enabled: true
        ).save(failOnError: true)
        def officer = SecUser.findByUsername('officer') ?: new SecUser(
            username: 'officer',
            password: springSecurityService.encodePassword('officer'),
            enabled: true
        ).save(failOnError: true)

        def coachRole = SecRole.findByAuthority('ROLE_COACH') ?: new SecRole(authority: 'ROLE_COACH').save(failOnError: true)
        def clubAdminRole = SecRole.findByAuthority('ROLE_CLUBADMIN') ?: new SecRole(authority: 'ROLE_CLUBADMIN').save(failOnError: true)
        def officerRole = SecRole.findByAuthority('ROLE_OFFICER') ?: new SecRole(authority: 'ROLE_OFFICER').save(failOnError: true)

        if (!manager1.authorities.contains(coachRole)) {
            SecUserSecRole.create manager1, coachRole
        }
        if (!manager2.authorities.contains(coachRole)) {
            SecUserSecRole.create manager2, coachRole
        }
        if (!clubAdmin.authorities.contains(clubAdminRole)) {
            SecUserSecRole.create clubAdmin, clubAdminRole
        }
        if (!clubAdmin.authorities.contains(coachRole)) {
            SecUserSecRole.create clubAdmin, coachRole
        }
        if (!officer.authorities.contains(clubAdminRole)) {
            SecUserSecRole.create officer, clubAdminRole
        }
        if (!officer.authorities.contains(officerRole)) {
            SecUserSecRole.create officer, officerRole
        }
        if (!officer.authorities.contains(coachRole)) {
            SecUserSecRole.create officer, coachRole
        }

        // workaround for crappy reindix bug in searchableService that prevents player table
        // being reindexed
        try {
            searchableService.reindex()
        } catch (Exception ex) {
            println "Normal exception from searchable reindex: $ex"
        }
        Player.reindex()
    }
    def destroy = {
    }
}
