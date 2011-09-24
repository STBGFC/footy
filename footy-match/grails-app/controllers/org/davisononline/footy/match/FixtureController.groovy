package org.davisononline.footy.match

import org.davisononline.footy.core.Team
import grails.plugins.springsecurity.Secured
import net.fortuna.ical4j.model.ContentBuilder
import net.fortuna.ical4j.model.property.DtStamp


@Secured(["ROLE_COACH"])
class FixtureController {

    private static final String ICAL_DTFORMAT = "yyyyMMdd'T'HHmmSS"

    def footyMatchService
    

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    @Secured(["permitAll"])
    def list = {
        def myteam = Team.get(params.id)
        if (!myteam) {
            response.sendError 404
        }

        [fixtures: footyMatchService.getAllFixtures(myteam), myteam: myteam]
    }

    def createDialog = {
        Team team = Team.get(params.id)
        if (!team) {
            flash.message = "No such team found"
            return
        }
        def oppos = Team.findAllByAgeBand(team.ageBand, [sort:"club.name", order: "asc"])
        render (template: 'createDialog', model: [teamInstance: team, oppositionTeams: oppos], contentType: 'text/plain', plugin: 'footy-match')
    }

    def save = {
        def fixtureInstance = new Fixture(params)

        if (params.opposition != "-1") {
            def opp = Team.get(params.opposition)
            fixtureInstance.opposition = "${opp.club.name} ${opp}"
        }
        else
            fixtureInstance.opposition = params.opposition2

        if (!fixtureInstance.save(flush: true)) {
            def msg = "Unable to save fixture [${Fixture}]; " + fixtureInstance.errors
            log.error msg
            response.status = 500
        }
        render template: 'fixtureList', model: [fixtures: footyMatchService.getFixtures(fixtureInstance.team), myteam: fixtureInstance.team], plugin: 'footy-match', contentType: 'text/plain'
    }

    /**
     * update the result and 9optionally) the match report
     */
    def saveResult = {
        def fx = Fixture.get(params.id)

        if (!fx) {
            log.error "No fixture found to update"
            response.status = 404
        }

        fx.played = (params.homeGoalsFullTime != null && params.homeGoalsFullTime != "")
        fx.extraTime = (params.homeGoalsExtraTime != null && params.homeGoalsExtraTime != "")
        fx.penalties = (params.homeGoalsPenalties != null && params.homeGoalsPenalties != "")

        if (!fx.played) {
            params.homeGoalsFullTime = 0
            params.awayGoalsFullTime = 0
            params.homeGoalsHalfTime = 0
            params.awayGoalsHalfTime = 0
        }
        if (!fx.extraTime) {
            params.homeGoalsExtraTime = 0
            params.awayGoalsExtraTime = 0
        }
        if (!fx.penalties) {
            params.homeGoalsPenalties = 0
            params.awayGoalsPenalties = 0
        }

        fx.properties = params
                
        if (!fx.save(flush: true)) {
            def msg = "Unable to save fixture [${fx}]; " + fx.errors
            log.error msg
            return
        }
        render template: 'fixtureList', model: [fixtures: footyMatchService.getFixtures(fx.team), myteam: fx.team], plugin: 'footy-match', contentType: 'text/plain'
    }

    def editDialog = {
        def fixtureInstance = Fixture.get(params.id)
        if (!fixtureInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'fixture.label', default: 'Fixture'), params.id])}"
            redirect(action: "list")
        }
        else {
            render template: 'editDialog', model:[fixtureInstance: fixtureInstance], contentType: 'text/plain', plugin: 'footy-match'
        }
    }

    def delete = {
        def fixtureInstance = Fixture.get(params.fixtureId)
        def myteam = Team.get(params.teamId)
        if (fixtureInstance && myteam) {
            try {
                fixtureInstance.delete(flush: true)
                render template: 'fixtureList', model: [fixtures: footyMatchService.getFixtures(myteam), myteam: myteam], plugin: 'footy-match', contentType: 'text/plain'
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'fixture.label', default: 'Fixture'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'fixture.label', default: 'Fixture'), params.id])}"
            redirect(action: "list")
        }
    }

    /**
     * output a standard calendar file of all the fixtures for this team.
     */
    @Secured(["permitAll"])
    def calendar = {

        def t = Team.get(params?.id)
        if (!t) {
            response.status = 404
            return
        }

        def fixtures = Fixture.findAllByTeam(t, [sort: 'dateTime', order: 'asc'])

        def link = g.createLink(
            absolute:true,
            controller:'fixture',
            action:'list',
            id:t.id
        ).toString()

        def builder = new ContentBuilder()
        def calendar = builder.calendar() {
            prodid('-//Footy//iCal4j 1.0//EN')
            calscale('GREGORIAN')
            version('2.0')
            method('PUBLISH')

            fixtures.each { f->

                def cal = Calendar.instance
                cal.time = f.dateTime
                cal.add(Calendar.MINUTE,90)

                vevent() {
                    uid(f.guid)
                    summary(f.toString())
                    dtstamp(new DtStamp())
                    dtstart(f.dateTime.format(ICAL_DTFORMAT))
                    dtend(cal.time.format(ICAL_DTFORMAT))
                    action('DISPLAY')
                    if (f.homeGame) location(t.club?.address?.postCode)
                    description(f.matchReport ?: link)
                }

            }
        }

        response.setHeader(
            "Content-disposition",
            "attachment; filename=${URLEncoder.encode(t.toString().replace(' ',''),'UTF-8')}-fixtures.ics"
        )
        render text:calendar, contentType: 'text/calendar'
        
    }
}
