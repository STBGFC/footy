package org.davisononline.footy.tournament

/**
 * @author darren
 */
class TournamentController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        if (!params.sort) {
            params.sort = "startDate"
            params.order = "asc"
        } 
        [tournamentInstanceList: Tournament.list(params), tournamentInstanceTotal: Tournament.count()]
    }

    def create = {
        def tournamentInstance = new Tournament()
        tournamentInstance.properties = params
        render(view: "edit", model: [tournamentInstance: tournamentInstance])
    }

    def save = {
        def tournamentInstance = new Tournament(params)
        if (tournamentInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'tournament.label', default: 'Tournament'), tournamentInstance.id])}"
            redirect(action: "list")
        }
        else {
            render(view: "edit", model: [tournamentInstance: tournamentInstance])
        }
    }
    
    def entryList = {
        def t = Tournament.get(params.id)
        [entries: t.entries]
    }

    def show = {
        display("show")
    }

    def edit = {
        def t = Tournament.get(params.id)
        // can't edit if entries already made
        if (t?.entries.size() > 0) {
            flash.message = "${message(code: 'org.davisononline.footy.tournament.entriesexist', default: 'Entries already exist - cannot edit tournament')}"
            redirect(action: "list", params: params)
        }
        else
            display("edit")
    }

    private display(action) {
        def tournamentInstance = Tournament.get(params.id)
        if (!tournamentInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'tournament.label', default: 'Tournament'), params.id])}"
            redirect(action: "list")
        }
        else {
            render (view: action, model:[tournamentInstance: tournamentInstance])
        }
    }
    
    def update = {
        def tournamentInstance = Tournament.get(params.id)
        if (tournamentInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (tournamentInstance.version > version) {
                    
                    tournamentInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'tournament.label', default: 'Tournament')] as Object[], "Another user has updated this Tournament while you were editing")
                    render(view: "edit", model: [tournamentInstance: tournamentInstance])
                    return
                }
            }
            tournamentInstance.properties = params
            if (!tournamentInstance.hasErrors() && tournamentInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'tournament.label', default: 'Tournament'), tournamentInstance.id])}"
                redirect(action: "list")
            }
            else {
                render(view: "edit", model: [tournamentInstance: tournamentInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'tournament.label', default: 'Tournament'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def tournamentInstance = Tournament.get(params.id)
        if (tournamentInstance) {
            try {
                tournamentInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'tournament.label', default: 'Tournament'), tournamentInstance.name])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'tournament.label', default: 'Tournament'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'tournament.label', default: 'Tournament'), params.id])}"
            redirect(action: "list")
        }
    }
}
