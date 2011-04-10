package org.davisononline.footy.core

import grails.plugins.springsecurity.Secured

/**
 * admin controller for Person operations
 */
@Secured(['ROLE_CLUB_ADMIN'])
class PersonController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 25, 100)
        if (!params.sort) params.sort = 'familyName'
        def l = Person.findAllByEligibleParent(true, params)
        [personInstanceList: l, personInstanceTotal: Person.countByEligibleParent(true)]
    }

    def create = {
        def personInstance = new Person()
        personInstance.properties = params
        render(view: 'edit', model:[personInstance: personInstance])
    }

    def save = { PersonCommand cmd ->
        def p = cmd.toPerson()
        if (!p.validate()) {
            cmd.errors = p.errors
            render(view: "edit", model: [personCommand: p])
        }
        else {
            p.save(flush: true)
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'person.label', default: 'Person'), p])}"
            redirect(action: "list")
        }
    }

    /**
     * checks to see if the Person being edited is a Player and edits the Player
     * instead if so.
     */
    def edit = {
        def personInstance = Person.get(params.id)
        if (!personInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])}"
            redirect(action: "list")
        }
        else {
            def player = Player.findByPerson(personInstance)
            if (player)
                redirect controller: 'player', action: 'edit', id: player.id
            return [personCommand: personInstance]
        }
    }

    def update = { PersonCommand cmd ->
        def personInstance = Person.get(params.id)
        if (personInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (personInstance.version > version) {
                    
                    personInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'person.label', default: 'Person')] as Object[], "Another user has updated this Person while you were editing")
                    render(view: "edit", model: [personInstance: personInstance])
                    return
                }
            }
            
            def submitted = cmd.toPerson()
            personInstance.properties = submitted.properties
            if (!personInstance.hasErrors() && personInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'person.label', default: ''), personInstance])}"
                redirect(action: "list")
            }
            else {
                render(view: "edit", model: [personCommand: personInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def personInstance = Person.get(params.id)
        if (personInstance) {
            try {
                personInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'person.label', default: 'Person'), params.id])}"
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'person.label', default: 'Person'), params.id])}"
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])}"
        }
        redirect(action: "list")
    }

    def assignQualification = {
        [qualificationTypes: QualificationType.listOrderByCategory(), personId: params.id]
    }

    def addQualification = {
        def qual = new Qualification(params)
        def p=Person.get(params.personId)
        p.addToQualifications(qual).save()
        // text/plain prevents sitemesh decoreation
        render template: '/person/qualificationsList', plugin: 'footy-core', model: [person: p], contentType: 'text/plain'
    }

    /*
     * URL mapping: /person/delQualification/${personId}/${qualificationId}
     */
    def delQualification = {
        def p = Person.get(params.personId)
        try {
            def q = p.qualifications.find {it.id = Long.valueOf(params?.qualificationId)}
            if (q) {
                p.removeFromQualifications(q)
                p.save()
            }
        }
        catch (Exception ex) {
            log.warn("Unable to delete qualification: $ex")
        }
        // text/plain prevents sitemesh decoreation
        render template: '/person/qualificationsList', plugin: 'footy-core', model: [person: p], contentType: 'text/plain'
    }
}
