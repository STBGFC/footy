package org.davisononline.footy.core

import grails.plugins.springsecurity.Secured

/**
 * controller methods for CRUD on Team
 */
@Secured(["ROLE_CLUB_ADMIN"])
class TeamController {

    def registrationService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [teamInstanceList: Team.list(params), teamInstanceTotal: Team.count()]
    }

    def create = {
        def teamInstance = new Team()
        teamInstance.properties = params
        render(view: "edit", model: [teamInstance: teamInstance, managers: getManagers()])
    }

    def save = {
        def teamInstance = new Team(params)
        if (teamInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'team.label', default: 'Team'), teamInstance])}"
            redirect action: "list"
        }
        else {
            render(view: "edit", model: [teamInstance: teamInstance, managers: getManagers()])
        }
    }

    def edit = {
        def teamInstance = Team.get(params.id)
        if (!teamInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'team.label', default: 'Team'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [teamInstance: teamInstance, managers: getManagers(), players: Player.findAllByTeam(teamInstance, [sort:"person.familyName", order:"asc"])]
        }
    }

    def update = {
        def teamInstance = Team.get(params.id)
        if (teamInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (teamInstance.version > version) {
                    
                    teamInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'team.label', default: 'Team')] as Object[], "Another user has updated this Team while you were editing")
                    render(view: "edit", model: [teamInstance: teamInstance, managers: getManagers()])
                    return
                }
            }
            teamInstance.properties = params
            if (!teamInstance.hasErrors() && teamInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'team.label', default: 'Team'), teamInstance.id])}"
                redirect(action: "list")
            }
            else {
                render(view: "edit", model: [teamInstance: teamInstance, managers: getManagers()])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'team.label', default: 'Team'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def teamInstance = Team.get(params.id)
        if (teamInstance) {
            try {
                teamInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'team.label', default: 'Team'), params.id])}"
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'team.label', default: 'Team'), params.id])}"
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'team.label', default: 'Team'), params.id])}"
        }

        redirect(action: "list")
    }

    /**
     * create the league registration form PDF from the current team
     * and players, sending direct to the ServletOutputStream
     *
     * @return
     */
    def leagueForm = {
        def teamInstance = Team.get(params.id)
        if (teamInstance) {
            try {
                response.contentType = 'application/octet-stream'
                response.setHeader 'Content-disposition', 'attachment; filename="registrationform.pdf"'
                def out = response.outputStream
                registrationService.generateRegistrationForm(teamInstance, out)
                out.flush()
                out.close()
                return null
            }
            catch (Exception e) {
                log.error(e)
                render view: 'error'
            }
        }
    }

    private getManagers() {
        Person.executeQuery(
                "select distinct q.person from Qualification q where q.type.category=:category order by q.person.familyName asc",
                [category: QualificationType.COACHING])
    }
}
