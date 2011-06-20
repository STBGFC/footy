import org.davisononline.footy.core.*
import org.davisononline.footy.tournament.*

class BootStrap {

    def searchableService

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
