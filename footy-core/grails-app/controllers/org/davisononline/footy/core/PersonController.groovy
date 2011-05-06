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

    def qualifications = {
        params.max = Math.min(params.max ? params.int('max') : 25, 100)
        params.sort = 'expiresOn'
        params.order = 'asc'
        def sixMonths = (new Date()) + 180
        def l = Qualification.findAllByExpiresOnLessThan(sixMonths, params)
        [qualifications: l, qualificationsTotal: Qualification.countByExpiresOnLessThan(sixMonths)]
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 25, 100)
        if (!params.sort) params.sort = 'familyName'
        def l = Person.findAllByEligibleParent(true, params)
        [personInstanceList: l, personInstanceTotal: Person.countByEligibleParent(true)]
    }

    def create = {
        def p = new Person(params)
        render(view: 'edit', model:[personCommand: p])
    }

    def save = {
        def p = new Person(params)
        if (!p.address?.validate() | !p.validate()) {
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

    def update = {
        def personInstance = Person.get(params.id)
        if (personInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (personInstance.version > version) {
                    
                    personInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'person.label', default: 'Person')] as Object[], "Another user has updated this Person while you were editing")
                    render(view: "edit", model: [personCommand: personInstance])
                    return
                }
            }
            personInstance.properties = params
            if (personInstance.address.validate() && !personInstance.hasErrors() && personInstance.save(flush: true)) {
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

        def p

        try {
            Qualification.withTransaction {status ->
                
                def qual = new Qualification(params)
                p = Person.get(params.personId)
                // remove expiring qualifications of the same type
                def old = p.qualifications.find {it.type == qual.type}
                old?.each {
                    p.removeFromQualifications(it)
                    it.delete()
                }

                // add new, save
                p.addToQualifications(qual)
            }
        }
        catch (Exception ex) {
            log.warn "Unable to add qualification: $ex"
        }

        // text/plain prevents sitemesh decoreation
        render template: '/person/qualificationsList', plugin: 'footy-core', model: [person: p], contentType: 'text/plain'
    }

    /*
     * URL mapping: /person/delQualification/${personId}/${qualificationId}
     */
    def delQualification = {
        def p = Person.get(params.personId)
        try {
            Qualification.withTransaction {status ->
                // WHY does this not cascade.. the qualification 'belongsTo' the Person
                def q = Qualification.get(params.qualificationId)
                q.delete()
                p.removeFromQualifications(q)
            }
        }
        catch (Exception ex) {
            log.warn("Unable to delete qualification: $ex")
        }
        // text/plain prevents sitemesh decoreation
        render template: '/person/qualificationsList', plugin: 'footy-core', model: [person: p], contentType: 'text/plain'
    }
}
