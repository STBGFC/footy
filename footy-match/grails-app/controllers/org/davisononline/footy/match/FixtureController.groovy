package org.davisononline.footy.match

import org.davisononline.footy.core.Team


class FixtureController {

    def footyMatchService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [fixtureInstanceList: Fixture.list(params), fixtureInstanceTotal: Fixture.count()]
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
        def myteam = fixtureInstance.homeTeam

        if (params.venue == 'away') {
            fixtureInstance.homeTeam = fixtureInstance.awayTeam
            fixtureInstance.awayTeam = myteam
        }

        if (fixtureInstance.save(flush: true)) {
            render template: 'fixtureList', model: [fixtures: footyMatchService.getFixtures(myteam), myteam: myteam], plugin: 'footy-match', contentType: 'text/plain'
        }
        else {
            render(view: "create", model: [fixtureInstance: fixtureInstance])
        }
    }

    def show = {
        def fixtureInstance = Fixture.get(params.id)
        if (!fixtureInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'fixture.label', default: 'Fixture'), params.id])}"
            redirect(action: "list")
        }
        else {
            [fixtureInstance: fixtureInstance]
        }
    }

    def delete = {
        def fixtureInstance = Fixture.get(params.id)
        if (fixtureInstance) {
            try {
                fixtureInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'fixture.label', default: 'Fixture'), params.id])}"
                redirect(action: "list")
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
}
