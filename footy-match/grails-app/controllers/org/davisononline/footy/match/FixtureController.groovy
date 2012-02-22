package org.davisononline.footy.match

import org.davisononline.footy.core.Team
import grails.plugins.springsecurity.Secured
import net.fortuna.ical4j.model.ContentBuilder
import net.fortuna.ical4j.model.property.DtStamp


@Secured(["ROLE_COACH"])
class FixtureController {

    private static final String ICAL_DTFORMAT = "yyyyMMdd'T'HHmmSS'Z'"

    def footyMatchService

    def personService

    def grailsApplication


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

    def addResult = {
        def fx = Fixture.get(params.id)

        if (!fx) {
            log.error "No fixture found to update"
            response.status = 404
            return
        }

        // can't add result before game has been played
        if (fx.dateTime > new Date()) {
            log.error "Attempt to add a result to a fixture not yet played"
            flash.message = "Cannot add a result for a fixture not yet played (${fx.dateTime.format('dd/MM HH:mm')})"
            redirect controller: 'team', action: 'show', ageBand: fx.team.ageBand, teamName: fx.team.name
        }

        // ok..
        else
            modelForResults(fx)

    }

    def modelForResults(fx) {
        def cfg = grailsApplication.config.org?.davisononline?.footy?.match
        def minAge = cfg.minimumagetopublishresults as Integer ?: 9
        def includeRefReport = ((cfg.referee.reportquestions as Integer) > 0)
        def m = [
            fixtureInstance: fx,
            minAge: minAge,
            includeRefReport: includeRefReport
        ]
        if (includeRefReport)
            m.putAll([
                refReportExists: (RefereeReport.findByFixture(fx) != null),
                availableReferees: personService.referees
            ])
        m
    }

    /**
     * update the result and (optionally) the match report
     */
    def saveResult = {
        def fx = Fixture.get(params.id)

        if (!fx) {
            log.error "No fixture found to update"
            response.status = 404
            return
        }

        def cfg = grailsApplication.config.org?.davisononline?.footy?.match
        def minAge = cfg.minimumagetopublishresults as Integer ?: 9
        def reportQuestions = cfg.referee?.reportquestions as Integer ?: 0
        def includeRefReport = (reportQuestions > 0)

        if (fx.team.ageBand >= minAge) {

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
        }

        // ref report
        RefereeReport report = new RefereeReport()
        if (includeRefReport && params['ref']?.size() > 0) {
            report.properties = params['ref']
            report.fixture = fx
            // auto type convertsion appears not to occur for the scores.. they remain as string
            if (report.referee) {
                report.scores = report.scores.collect {it as Integer}
                if ((report.scores-null).size() < reportQuestions) {
                    report.errors.rejectValue('scores', 'Please answer ALL questions in the report')
                    flash.message = 'Please answer ALL questions in the report'
                }
            }
            else
                report.scores = []

            if (report.hasErrors()) {
                render view: 'addResult', model: modelForResults(fx)
                return
            }
        }

        if (fx.hasErrors()) {
            render view: 'addResult', model: modelForResults(fx)
            return
        }

        def saved = (fx.save(flush: true) && ((includeRefReport && fx.homeGame) ? report.save(flush: true) : true))
        if (saved) {
            flash.message = "Fixture details ${params['ref']?.size() > 0 ? 'and referee report ' : ''}saved"
            redirect controller: 'team', action: 'show', params:[ageBand: fx.team.ageBand, teamName: fx.team.name]
        }
        else {
            flash.message = 'Unable to save fixture or report details'
            render view: 'addResult', model: modelForResults(fx)
            return
        }
    }

    def delete = {
        def fixtureInstance = Fixture.get(params.fixtureId)
        def myteam = Team.get(params.teamId)
        if (fixtureInstance && myteam) {
            try {
                footyMatchService.deleteFixture(fixtureInstance)
            }
            catch (Exception e) {
                response.status = 500
            }
        }
        else {
            response.status = 404
        }

        render template: 'fixtureList', model: [fixtures: footyMatchService.getFixtures(myteam), myteam: myteam], plugin: 'footy-match', contentType: 'text/plain'
    }

    /**
     * output a standard calendar file of all the fixtures for this team.
     */
    @Secured(["permitAll"])
    def calendar = {

        cache "content"

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
