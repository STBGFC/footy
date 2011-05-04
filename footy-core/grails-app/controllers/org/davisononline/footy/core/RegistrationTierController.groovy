package org.davisononline.footy.core

import grails.plugins.springsecurity.Secured
import org.codehaus.groovy.grails.commons.ConfigurationHolder

/**
 * controller methods for CRUD on RegistrationTier
 */
@Secured(["ROLE_CLUB_ADMIN"])
class RegistrationTierController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 20, 100)
        [
            registrationTierInstanceList: RegistrationTier.list(params),
            registrationTierInstanceTotal: RegistrationTier.count(),
            currency: ConfigurationHolder.config.org?.davisononline?.footy?.registration?.currency ?: "GBP"
        ]
    }

    def create = {
        def registrationTierInstance = new RegistrationTier()
        registrationTierInstance.properties = params
        render(view: "edit", model: [registrationTierInstance: registrationTierInstance])
    }

    def save = {
        def registrationTierInstance = new RegistrationTier(params)
        if (registrationTierInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'registrationTier.label', default: 'RegistrationTier'), registrationTierInstance.name])}"
            redirect action: "list"
        }
        else {
            render(view: "edit", model: [registrationTierInstance: registrationTierInstance])
        }
    }

    def edit = {
        def registrationTierInstance = RegistrationTier.get(params.id)
        if (!registrationTierInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'registrationTier.label', default: 'RegistrationTier'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [registrationTierInstance: registrationTierInstance]
        }
    }

    def update = {
        def registrationTierInstance = RegistrationTier.get(params.id)
        if (registrationTierInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (registrationTierInstance.version > version) {
                    
                    registrationTierInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'registrationTier.label', default: 'RegistrationTier')] as Object[], "Another user has updated this RegistrationTier while you were editing")
                    render(view: "edit", model: [registrationTierInstance: registrationTierInstance])
                    return
                }
            }
            registrationTierInstance.properties = params
            if (!registrationTierInstance.hasErrors() && registrationTierInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'registrationTier.label', default: 'RegistrationTier'), registrationTierInstance.id])}"
                redirect(action: "list")
            }
            else {
                render(view: "edit", model: [registrationTierInstance: registrationTierInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'registrationTier.label', default: 'RegistrationTier'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def registrationTierInstance = RegistrationTier.get(params.id)
        if (registrationTierInstance) {
            try {
                registrationTierInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'registrationTier.label', default: 'RegistrationTier'), params.id])}"
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'registrationTier.label', default: 'RegistrationTier'), params.id])}"
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'registrationTier.label', default: 'RegistrationTier'), params.id])}"
        }

        redirect(action: "list")
    }
}
