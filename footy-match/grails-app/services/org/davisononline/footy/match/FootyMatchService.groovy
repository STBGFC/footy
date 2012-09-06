package org.davisononline.footy.match

import org.davisononline.footy.core.Team
import org.davisononline.footy.core.utils.DateTimeUtils
import org.davisononline.footy.core.Person
import org.davisononline.footy.core.SecUser
import org.davisononline.footy.core.Player
import org.davisononline.footy.core.SecRole
import org.davisononline.footy.core.SecUserSecRole


class FootyMatchService {

    static transactional = true

    def mailService

    def springSecurityService


    /**
     * returns a list of all fixtures for the supplied team for the season
     *
     * @param team
     * @param n number of fixtures to return, defaults to 10
     * @return
     */
    def getAllFixtures(Team team) {
        def fc = Fixture.createCriteria()
        def list = fc.list () {
            eq ("team", team)
            ge ("dateTime", DateTimeUtils.getCurrentSeasonStart())
            order ("dateTime", "desc")
        }
        list
    }

    /**
     * returns a list of up to n fixtures for the supplied team from the current date
     * (ie future)
     *
     * @param team
     * @param n number of fixtures to return, defaults to 10
     * @return
     */
    def getFixtures(Team team, int n = 10) {
        getFixtures(team, n, new Date())
    }

    /**
     * returns a list of up to n fixtures for the supplied team from the supplied date
     * or any that are in the past but have not been played
     *
     * @param myteam
     * @param n
     * @param date
     * @return
     */
    def getFixtures(Team team, int n, Date date) {
        if (!team) return null
        def fc = Fixture.createCriteria()
        def list = fc.list (max:n) {
            eq ("team", team)
            ge ("dateTime", DateTimeUtils.getCurrentSeasonStart())
            or {
                ge ("dateTime", date)
                eq ("played", false)
            }
            order ("dateTime", "asc")
        }
        list
    }

    /**
     * returns a list of fixtures that are being played at home on a certain date
     *
     * @param date the date that the fixtures are being played
     * @return
     */
    def getHomeGamesOn(Date date) {
        Date from = DateTimeUtils.setMidnight(date)

        def fc = Fixture.createCriteria()
        def list = fc.list () {
            eq ("homeGame", true)
            eq ("played", false)
            and {
                ge ("dateTime", from)
                lt ("dateTime", from + 1)
            }
            order ("dateTime", "asc")
        }
        list
    }

    /**
     * returns a list of ref reports that are for fixtures on a certain date
     *
     * @param date the date that the fixtures are being played
     * @return
     */
    def getRefReportsOn(Date date) {
        Date from = DateTimeUtils.setMidnight(date)

        def rc = RefereeReport.createCriteria()
        def list = rc.list () {
            fixture {
                and {
                    ge ("dateTime", from)
                    lt ("dateTime", from + 1)
                }
                order ("dateTime", "asc")
            }
        }
        list
    }

    /**
     * updates the fixture list with committed resource allocations (refs,
     * pitches, changing rooms etc) and sends the notification emails to
     * managers and referees.  Additionally, creates a summary document for
     * downloading.
     */
    def saveResourceAllocations(fixtures) {

        // ensure we have a valid logged-in person with an email address to send the emails
        String username =  springSecurityService.authentication.name
        def user = SecUser.findByUsername(username)
        def person = Person.findByUser(user)
        if (!person) {
            throw new Exception("Logged in user has no valid email address.. cannot send confirmation emails")
        }

        Set<Person> refs = [] as Set

        // sort again by date/time which might have been modified since fetching from db
        fixtures = fixtures.sort().reverse()

        // save all and gather set of refs to send email to
        fixtures?.each {fixture ->
            if (fixture.referee) refs.add(fixture.referee)
            if (fixture.isDirty('dateTime')) fixture.adjustedKickOff = true

            if (!fixture.team.manager) {
                log.info "No manager for team $fixture.team - no email sent for resource allocations"
            }
            if (fixture.isDirty() || fixture.amendedResources) {
                try {
                    mailService.sendMail {
                        to      fixture.team.manager.email
                        from    person.email
                        subject "Pitch/Referee Confirmation for your ${fixture.team} game on ${fixture.dateTime.format('dd/MM/yyyy')}"
                        body    (view: '/email/match/managerResources',
                                 model: [fixture:fixture])
                    }
                }
                catch (Exception ex) {
                    log.warn("Unable to send email to manager $fixture.team.manager; $ex.message")
                }
            }

            fixture.save()
        }

        // Refs emails..
        refs?.each {ref ->
            def myFixtures = fixtures.grep{it.referee.id == ref.id}
            try {
                mailService.sendMail {
                    to      getRefEmail(ref)
                    from    person.email
                    subject "Fixture Confirmations for ${myFixtures[0].dateTime.format('dd/MM/yyyy')}"
                    body    (view: '/email/match/refereeResources',
                             model: [ref:ref, myFixtures:myFixtures])
                }
            }
            catch (Exception ex) {
                log.warn("Unable to send email to referee $ref; $ex.message")
            }
        }
    }

    /**
     * delete a Fixture, potentially emailing fixture secs. if resources were
     * allocated.  Any exception will be thrown back to the caller
     */
    def deleteFixture(fixtureInstance) {
        try {

            if (fixtureInstance.resources.size() > 0 || fixtureInstance.referee) {
                fixtureInstance.resources.clear()
                fixtureInstance.delete(flush: true)
                def fixSecEmails = getFixtureSec()*.email
                if (fixtureInstance.referee?.email) fixSecEmails << getRefEmail(fixtureInstance.referee)
                mailService.sendMail {
                    to      fixSecEmails
                    subject "Deleted Fixture ${fixtureInstance.dateTime.format('dd/MM/yyyy')}"
                    body    (view: '/email/match/deletedFixture',
                             model: [fixture:fixtureInstance])
                }
            }
            else
                fixtureInstance.delete(flush: true)
        }
        catch (Exception ex) {
            log.error("Failed to delete fixture or to email fixture sec(s) with deletion of fixture [$fixtureInstance]; ${ex.message}")
        }
    }

    /**
     * return a list of all fixture secretaries (ROLE_FIXTURE_ADMIN)
     */
    def getFixtureSec() {
        def fa = SecRole.findByAuthority("ROLE_FIXTURE_ADMIN")
        def secs = SecUserSecRole.findAllBySecRole(fa).secUser as List
        def persons = Person.findAllByUserInList(secs)
        persons
    }

    /**
     * get a ref's email address.  Might simply have her own, in which case it's returned,
     * but if the ref is a player without their owm, it will lookup the parent email
     * address and return that if available
     */
    def getRefEmail(ref) {
        def target = ref.email
        if (!target) {
            // might be a player.. send to guardian if so
            def g = Player.findByPerson(ref)
            target = g.guardian?.email ?: g.secondGuardian?.email
        }
        target
    }
}
