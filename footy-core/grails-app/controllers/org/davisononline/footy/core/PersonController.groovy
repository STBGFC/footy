package org.davisononline.footy.core

import grails.plugins.springsecurity.Secured
import org.davisononline.footy.core.utils.ImageUtils

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
        /*
         * I want to do Person.findAllByEligibleParent(true, params)
         * but the pagination fails because it does the select from the db, then filters the list, so I
         * get pages with 'gaps' where the non-eligibleParent records would be.  The hql version works
         * but now I lose sortableColumns as that would have to be specified in the order by clause
         */
        def l = Person.findAll("from Person p where eligibleParent = ? order by p.familyName", [true], params)
        [personInstanceList: l, personInstanceTotal: Person.countByEligibleParent(true)]
    }

    def create = {
        def p = new Person(params)
        render(view: 'edit', model:[personCommand: p])
    }

    def save = {
        def p = new Person(params)
        p.address = p.address ?:  new Address()
        if (!p.address?.validate() | !p.validate()) {
            render(view: "edit", model: [personCommand: p])
        }
        else {
            p.save(flush: true)
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'person.label', default: 'Person'), p])}"
            redirect(session.breadcrumb ? [uri: session.breadcrumb] : [action: "list"])
        }
    }

    /**
     * checks to see if the Person being edited is a Player and edits the Player
     * instead if so.
     */
    @Secured(["ROLE_COACH"]) // <-- TEMP
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

    @Secured(["ROLE_COACH"]) // <-- TEMP
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
            if (personInstance.address?.validate() && !personInstance.hasErrors() && personInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'person.label', default: ''), personInstance])}"
                redirect(session.breadcrumb ? [uri: session.breadcrumb] : [action: "list"])
            }
            else {
                render(view: "edit", model: [personCommand: personInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])}"
            redirect(session.breadcrumb ? [uri: session.breadcrumb] : [action: "list"])
        }
    }

    def delete = {
        def personInstance = Person.get(params.id)
        if (personInstance) {
            try {
                personInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'person.label', default: 'Person'), personInstance])}"
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'person.label', default: 'Person'), personInstance])}"
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])}"
        }
        redirect(session.breadcrumb ? [uri: session.breadcrumb] : [action: "list"])
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
                p.removeFromQualifications(q)
                q.delete()
            }
        }
        catch (Exception ex) {
            log.warn("Unable to delete qualification: $ex")
        }
        // text/plain prevents sitemesh decoreation
        render template: '/person/qualificationsList', plugin: 'footy-core', model: [person: p], contentType: 'text/plain'
    }

    /**
     * dialog for manager/coach photo to be uploaded
     */
    @Secured(["ROLE_COACH"])
    def photoUploadDialog = {
        render (template: 'photoUploadDialog', model: params, contentType: 'text/plain', plugin: 'footy-core')
    }

    /**
     * post action for team photo to be uploaded
     */
    @Secured(["ROLE_COACH"])
    def photoUpload = {
        def photo = request.getFile('photo')
        def p = Person.get(params.id)
        if (params.delete) {
            p.photo = null
            p.save(flush:true)
        }
        else if(!photo.empty) {
            def bytes = ImageUtils.convertImageToByteArray(ImageUtils.resize(photo.fileItem.tempFile, 66, 88), "PNG")
            p.photo = bytes
            p.save(flush:true)
        }
        def t = Team.get(params.teamId)
        redirect (controller: 'team', action: 'show', params:[ageBand: t.ageBand, teamName: t.name])
    }

    /**
     * render the actual team photo as an image source
     *
     * @return the bytes for the image
     */
    @Secured(["permitAll"])
    def photo = {
        cache "pics"
        def p = Person.get(params.id)
        writeImageBytesToResponse(p.photo, response)
    }

    private writeImageBytesToResponse(bytes, response) {
        response.contentType = "image/png"
        response.contentLength = bytes.length
        response.outputStream.write(bytes)
    }
}
