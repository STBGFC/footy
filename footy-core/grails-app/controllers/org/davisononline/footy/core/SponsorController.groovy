package org.davisononline.footy.core

import grails.plugins.springsecurity.Secured

@Secured(["ROLE_CLUB_ADMIN"])
class SponsorController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [sponsorInstanceList: Sponsor.list(params), sponsorInstanceTotal: Sponsor.count()]
    }

    def create = {
        def sponsorInstance = new Sponsor()
        sponsorInstance.properties = params
        render view:'edit', model:[sponsorInstance: sponsorInstance]
    }

    def save = {
        def sponsorInstance = new Sponsor(params)
        if (sponsorInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'sponsor.label', default: 'Sponsor'), sponsorInstance.name])}"
            redirect(action: "list")
        }
        else {
            render(view: "edit", model: [sponsorInstance: sponsorInstance])
        }
    }

    def edit = {
        def sponsorInstance = Sponsor.get(params.id)
        if (!sponsorInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'sponsor.label', default: 'Sponsor'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [sponsorInstance: sponsorInstance]
        }
    }

    def update = {
        def sponsorInstance = Sponsor.get(params.id)
        if (sponsorInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (sponsorInstance.version > version) {
                    
                    sponsorInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'sponsor.label', default: 'Sponsor')] as Object[], "Another user has updated this Sponsor while you were editing")
                    render(view: "edit", model: [sponsorInstance: sponsorInstance])
                    return
                }
            }
            sponsorInstance.properties = params
            if (!sponsorInstance.hasErrors() && sponsorInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'sponsor.label', default: 'Sponsor'), sponsorInstance.name])}"
                redirect(action: "list")
            }
            else {
                render(view: "edit", model: [sponsorInstance: sponsorInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'sponsor.label', default: 'Sponsor'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def sponsorInstance = Sponsor.get(params.id)
        if (sponsorInstance) {
            try {
                sponsorInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'sponsor.label', default: 'Sponsor'), params.id])}"
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'sponsor.label', default: 'Sponsor'), params.id])}"
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'sponsor.label', default: 'Sponsor'), params.id])}"
        }
        
        redirect(action: "list")
    }

    /**
     * render the sponsor logo
     */
    @Secured(["permitAll"])
    def logo = {
        cache "pics"
        def sp = Sponsor.get(params.id)
        renderImage sp?.logo
    }

    private renderImage(byte[] bytes) {
        if (!bytes) {
            response.sendError(404)
            return
        }
        response.contentType = "image/png"
    	response.contentLength = bytes?.length
    	response.outputStream.write(bytes)
        response.outputStream.close()
    }
}
