package org.davisononline.footy.core

class PlayerController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 25, 100)
        if (!params.sort) params.sort = 'person.familyName'
        def l = Player.list(params)
        [playerInstanceList: l, playerInstanceTotal: l.size()]
    }

    def create = {
        def playerInstance = new Player()
        playerInstance.properties = params
        render(view: 'edit', model:[playerInstance: playerInstance])
    }

    def save = { PlayerCommand cmd ->
        def p = cmd.toPlayer()
        if (!p.validate()) {
            cmd.errors = p.errors
            render(view: "edit", model: [playerCommand: p])
        }
        else {
            p.save(flush: true)
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'player.label', default: 'Player'), p])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def playerInstance = Player.get(params.id)
        if (!playerInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'player.label', default: 'Player'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [playerInstance: playerInstance, parents: Person.findAllEligibleParent()]
        }
    }

    def update = { PlayerCommand cmd ->
        def playerInstance = Player.get(params.id)
        if (playerInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (playerInstance.version > version) {

                    playerInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'player.label', default: 'Player')] as Object[], "Another user has updated this Player while you were editing")
                    render(view: "edit", model: [playerInstance: playerInstance])
                    return
                }
            }

            def submitted = cmd.toPlayer()
            playerInstance.properties = submitted.properties
            playerInstance.guardian = Person.get(cmd.parentId)
            playerInstance.secondGuardian = Person.get(cmd.secondParentId)

            if (!playerInstance.hasErrors() && playerInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'player.label', default: ''), playerInstance])}"
                redirect(action: "list")
            }
            else {
                render(view: "edit", model: [playerCommand: playerInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'player.label', default: 'Player'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def playerInstance = Player.get(params.id)
        if (playerInstance) {
            try {
                playerInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'player.label', default: 'Player'), params.id])}"
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'player.label', default: 'Player'), params.id])}"
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'player.label', default: 'Player'), params.id])}"
        }
        redirect(action: "list")
    }
}
