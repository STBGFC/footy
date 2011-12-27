package org.davisononline.footy.match

import org.davisononline.footy.core.Team
import org.davisononline.footy.core.utils.DateTimeUtils
import org.davisononline.footy.core.Person
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.davisononline.footy.core.SecUser
import org.davisononline.footy.core.utils.TemplateUtils

class FootyMatchService {

    static transactional = true

    def mailService

    def springSecurityService

    def refereeEmailBody = ConfigurationHolder.config?.org?.davisononline?.footy?.match?.referee?.emailbody
    def managerEmailBody = ConfigurationHolder.config?.org?.davisononline?.footy?.match?.manager?.emailbody


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
            order ("dateTime", "asc")
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
        def fc = Fixture.createCriteria()
        def list = fc.list (max:n) {
            eq ("team", team)
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
                        subject """Pitch/Referee Confirmation for your ${fixture.team} game on ${fixture.dateTime.format('dd/MM/yyyy')}"""
                        body    TemplateUtils.eval(
                                    managerEmailBody,
                                    [fixture:fixture]
                                )
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
            def myFixtures = fixtures.grep{it.referee == ref}
            try {
                mailService.sendMail {
                    to      ref.email
                    from    person.email
                    subject "Fixture Confirmations for ${myFixtures[0].dateTime.format('dd/MM/yyyy')}"
                    body    TemplateUtils.eval(
                                refereeEmailBody,
                                [ref:ref, myFixtures:myFixtures]
                            )
                }
            }
            catch (Exception ex) {
                log.warn("Unable to send email to referee $ref; $ex.message")
            }
        }
    }
}
