package org.davisononline.footy.core

import grails.plugins.springsecurity.Secured


@Secured(["ROLE_CLUB_ADMIN"])
class AgeGroupController {

    def personService


    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 25, 100)
        params.sort = params.sort ?: 'year'
        [ageGroupInstanceList: AgeGroup.list(params), ageGroupInstanceTotal: AgeGroup.count()]
    }

    def create = {
        def ageGroupInstance = new Division()
        ageGroupInstance.properties = params

        render view:'edit', model:[ageGroupInstance: ageGroupInstance]
    }
    
    def save = {
        def ageGroupInstance = new AgeGroup(params)
        if (ageGroupInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'ageGroup.label', default: 'Age Group'), ageGroupInstance.toString()])}"
            redirect(action: "list")
        }
        else {
            render(view: "edit", model: [ageGroupInstance: ageGroupInstance])
        }
    }

    def edit = {
        def ageGroupInstance = AgeGroup.get(params.id)
        if (!ageGroupInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'team.label', default: 'AgeGroup'), params.id])}"
            redirect(session.breadcrumb ? [uri: session.breadcrumb] : [action: "list"])
        }
        else {
            return [ageGroupInstance: ageGroupInstance, coordinators: personService.getCrbs()]
        }
    }
    
    def update = {
        def ageGroupInstance = AgeGroup.get(params.id)
        if (ageGroupInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (ageGroupInstance.version > version) {
                    
                    ageGroupInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'ageGroup.label', default: 'AgeGroup')] as Object[], "Another user has updated this Age Group while you were editing")
                    render(view: "edit", model: [ageGroupInstance: ageGroupInstance])
                    return
                }
            }
            ageGroupInstance.properties = params
            if (!ageGroupInstance.hasErrors() && ageGroupInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'ageGroup.label', default: 'AgeGroup'), ageGroupInstance])}"
                redirect(action: "list")
            }
            else {
                render(view: "edit", model: [ageGroupInstance: ageGroupInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'ageGroup.label', default: 'AgeGroup'), params.id])}"
            redirect(action: "list")
        }
    }

}
