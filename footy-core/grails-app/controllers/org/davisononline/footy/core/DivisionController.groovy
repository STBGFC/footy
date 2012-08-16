package org.davisononline.footy.core

import grails.plugins.springsecurity.Secured

@Secured(["ROLE_CLUB_ADMIN"])
class DivisionController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(controller: "league", action: "list")
    }

    def list = {
        redirect(controller: "league", action: "list")
    }

    def create = {
        if (!params['league.id']) response.sendError(400, "No league supplied to create division in")

        def divisionInstance = new Division()
        divisionInstance.properties = params

        render view:'edit', model:[divisionInstance: divisionInstance]
    }

    def save = {
        def divisionInstance = new Division(params)
        if (divisionInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'division.label', default: 'Division'), divisionInstance.toString()])}"
            redirect(controller: "league", action: "edit", id: divisionInstance.league.id)
        }
        else {
            render(view: "edit", model: [divisionInstance: divisionInstance])
        }
    }

    def edit = {
        def divisionInstance = Division.get(params.id)
        if (!divisionInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'division.label', default: 'Division'), params.id])}"
            redirect(controller: "league", action: "edit", id: divisionInstance.league.id)
        }
        else {
            return [divisionInstance: divisionInstance]
        }
    }

    def update = {
        def divisionInstance = Division.get(params.id)
        if (divisionInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (divisionInstance.version > version) {

                    divisionInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'division.label', default: 'Division')] as Object[], "Another user has updated this Division while you were editing")
                    render(view: "edit", model: [divisionInstance: divisionInstance])
                    return
                }
            }
            divisionInstance.properties = params
            if (!divisionInstance.hasErrors() && divisionInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'division.label', default: 'Division'), divisionInstance.toString()])}"
                redirect(controller: "league", action: "edit", id: divisionInstance.league.id)
            }
            else {
                render(view: "edit", model: [divisionInstance: divisionInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'division.label', default: 'Division'), params.id])}"
            redirect(controller: "league", action: "edit", id: divisionInstance.league.id)
        }
    }

    def delete = {
        def divisionInstance = Division.get(params.id)
        if (divisionInstance) {
            try {
                divisionInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'division.label', default: 'Division'), params.id])}"
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'division.label', default: 'Division'), params.id])}"
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'division.label', default: 'Division'), params.id])}"
        }

        redirect(controller: "league", action: "edit", id: divisionInstance.league.id)
    }
}
