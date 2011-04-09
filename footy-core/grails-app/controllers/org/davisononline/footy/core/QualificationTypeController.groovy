package org.davisononline.footy.core

class QualificationTypeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [qualificationTypeInstanceList: QualificationType.list(params), qualificationTypeInstanceTotal: QualificationType.count()]
    }

    def create = {
        def qualificationTypeInstance = new QualificationType()
        qualificationTypeInstance.properties = params
        render view: "edit", model:[qualificationTypeInstance: qualificationTypeInstance]
    }

    def save = {
        def qualificationTypeInstance = new QualificationType(params)
        if (qualificationTypeInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'qualificationType.label', default: 'QualificationType'), qualificationTypeInstance.name])}"
            redirect(action: "list")
        }
        else {
            render(view: "edit", model: [qualificationTypeInstance: qualificationTypeInstance])
        }
    }

    def edit = {
        def qualificationTypeInstance = QualificationType.get(params.id)
        if (!qualificationTypeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'qualificationType.label', default: 'QualificationType'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [qualificationTypeInstance: qualificationTypeInstance]
        }
    }

    def update = {
        def qualificationTypeInstance = QualificationType.get(params.id)
        if (qualificationTypeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (qualificationTypeInstance.version > version) {
                    
                    qualificationTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'qualificationType.label', default: 'QualificationType')] as Object[], "Another user has updated this QualificationType while you were editing")
                    render(view: "edit", model: [qualificationTypeInstance: qualificationTypeInstance])
                    return
                }
            }
            qualificationTypeInstance.properties = params
            if (!qualificationTypeInstance.hasErrors() && qualificationTypeInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'qualificationType.label', default: 'QualificationType'), qualificationTypeInstance.name])}"
                redirect(action: "list")
            }
            else {
                render(view: "edit", model: [qualificationTypeInstance: qualificationTypeInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'qualificationType.label', default: 'QualificationType'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def qualificationTypeInstance = QualificationType.get(params.id)
        if (qualificationTypeInstance) {
            try {
                qualificationTypeInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'qualificationType.label', default: 'QualificationType'), params.id])}"
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'qualificationType.label', default: 'QualificationType'), params.id])}"
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'qualificationType.label', default: 'QualificationType'), params.id])}"
        }
        
        redirect(action: "list")
    }
}
