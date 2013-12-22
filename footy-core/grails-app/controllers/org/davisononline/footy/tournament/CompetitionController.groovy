package org.davisononline.footy.tournament

import grails.plugins.springsecurity.Secured
import org.davisononline.footy.core.Division


@Secured(["ROLE_TOURNAMENT_ADMIN"])
class CompetitionController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(controller: 'tournament', action: "list")
    }

    def list = {
        redirect(controller: 'tournament', action: "list")
    }

    def create = {
        if (!params['tournament.id']) response.sendError(400, "No tournament supplied to create competition in")

        def competitionInstance = new Competition()
        competitionInstance.properties = params

        render(view:'edit', model:[competitionInstance: competitionInstance])
    }

    def save = {
        def competitionInstance = new Competition(params)
        if (competitionInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'competition.label', default: 'Competition'), competitionInstance.name])}"
            redirect(controller: "tournament", action: "edit", id: competitionInstance.tournament.id)
        }
        else {
            render(view: "edit", model: [competitionInstance: competitionInstance])
        }
    }

    def edit = {
        def competitionInstance = Competition.get(params.id)
        if (!competitionInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'competition.label', default: 'Competition'), params.id])}"
            redirect(controller: "tournament", action: "edit", id: competitionInstance.tournament.id)
        }
        else {
            return [competitionInstance: competitionInstance]
        }
    }

    def update = {
        def competitionInstance = Competition.get(params.id)
        if (competitionInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (competitionInstance.version > version) {
                    
                    competitionInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'competition.label', default: 'Competition')] as Object[], "Another user has updated this Competition while you were editing")
                    render(view: "edit", model: [competitionInstance: competitionInstance])
                    return
                }
            }
            competitionInstance.properties = params
            if (!competitionInstance.hasErrors() && competitionInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'competition.label', default: 'Competition'), competitionInstance.name])}"
                redirect(controller: "tournament", action: "edit", id: competitionInstance.tournament.id)
            }
            else {
                render(view: "edit", model: [competitionInstance: competitionInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'competition.label', default: 'Competition'), params.id])}"
            redirect(controller: 'tournament', action: 'edit', id: competitionInstance.tournament.id)
        }
    }

    def delete = {
        def competitionInstance = Competition.get(params.id)
        if (competitionInstance) {
            try {
                competitionInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'competition.label', default: 'Competition'), params.id])}"
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'competition.label', default: 'Competition'), params.id])}"
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'competition.label', default: 'Competition'), params.id])}"
        }

        redirect(controller: "tournament", action: "list", id: competitionInstance.tournament.id)
    }
}
