import org.davisononline.footy.core.*
import org.davisononline.footy.tournament.*

class BootStrap {

    def searchableService
    def springSecurityService

    def init = { servletContext ->
        // set up a couple of logins that can be used in tests
        def coachRole = SecRole.findByAuthority('ROLE_COACH') ?: new SecRole(authority: 'ROLE_COACH').save(failOnError: true)
        def clubAdminRole = SecRole.findByAuthority('ROLE_CLUBADMIN') ?: new SecRole(authority: 'ROLE_CLUBADMIN').save(failOnError: true)
        def officerRole = SecRole.findByAuthority('ROLE_OFFICER') ?: new SecRole(authority: 'ROLE_OFFICER').save(failOnError: true)

        def manager
        5.times {i->
            manager = SecUser.findByUsername("Manager${i}") ?: new SecUser(
                username: "Manager${i}",
                password: springSecurityService.encodePassword("Manager${i}"),
                enabled: true
            ).save(failOnError: true)

            if (!manager.authorities.contains(coachRole)) {
                SecUserSecRole.create manager, coachRole
            }
        }
        def clubAdmin = SecUser.findByUsername('clubAdmin') ?: new SecUser(
            username: 'clubAdmin',
            password: springSecurityService.encodePassword('clubAdmin1'),
            enabled: true
        ).save(failOnError: true)
        def officer = SecUser.findByUsername('officer') ?: new SecUser(
            username: 'officer',
            password: springSecurityService.encodePassword('Officer1'),
            enabled: true
        ).save(failOnError: true)

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

        // leagues
        def l
        def dn1
        if (League.count() == 0) {
            l=new League(name:"North").save()
            dn1 = new Division(league: l, name: "Northern Premier", index: 0).save()
            def dn2 = new Division(league: l, name: "Northern Second", index: 1).save()
            def l2=new League(name:"South").save()
            def ds1 = new Division(league: l2, name: "Southern Premier", index: 0).save()
            def ds2 = new Division(league: l2, name: "Southern Second", index: 1).save()
        }

        // common address
        def commune = new Address(
            house: '1',
            address:'Some St.', 
            town:'Anytown', 
            postCode:'GU1 1DB'
        ).save(flush:true)

        // club data
        def clubSec
        def club
        if (!Club.homeClub) {
            clubSec = new Person(
                givenName: 'John',
                familyName: 'Secretary',
                phone1: '07000000000',
                email: 'john.secretary@examplefc.com',
                eligibleParent: true,
                address: commune
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
            eligibleParent: true,
            address: commune,
            user: SecUser.findByUsername('Manager1')
        ).save(flush: true)
        def p2 = new Person(
            givenName: 'Jules',
            familyName: 'Parent',
            phone1: '07000000002',
            email: 'jules.parent@examplefc.com',
            eligibleParent: true,
            user:  SecUser.findByUsername('Manager2')
        ).save(flush: true)
        def p3 = new Person(
            givenName: 'Jeff',
            familyName: 'Manager',
            phone1: '07000000003',
            email: 'jeff.manager@examplefc.com',
            eligibleParent: true,
            user:  SecUser.findByUsername('Manager3')
        ).save(flush: true)

        // team
        def t = new Team(
            league: l,
            division: dn1,
            club: club,
            manager: p3,
            ageBand: 11,
            name: 'Rascals'
        ).save()
        if (RegistrationTier.count() == 0) {
            def date = new Date() + 300
            new RegistrationTier(
                    name: "Junior",
                    amount: 60.00,
                    siblingDiscount: 15.00,
                    validUntil: date
            ).save()
            new RegistrationTier(
                    name: "Senior",
                    amount: 80.00,
                    siblingDiscount: 15.00,
                    validUntil: date
            ).save()
        }        
        
        def sa = SecUser.findByUsername('sa')
        sa.passwordExpired = false
        sa.save(failOnError: true)

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
