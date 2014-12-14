package org.davisononline.footy.core

import grails.plugins.springsecurity.Secured
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.davisononline.footy.core.utils.ImageUtils

/**
 * admin controller for Person operations
 */
@Secured(['ROLE_CLUB_ADMIN'])
class PersonController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST", addQualification: "POST"]

    def personService

    def exportService

    def grailsApplication


    def index = {
        redirect(action: "list", params: params)
    }

    def qualifications = {
        params.max = Math.min(params.max ? params.int('max') : 100, 200)
        params.sort = 'expiresOn'
        params.order = 'asc'
        params.cache = true
        def sixMonths = (new Date()) + 180
        def l = Qualification.findAllByExpiresOnLessThan(sixMonths, params)
        [qualifications: l, qualificationsTotal: Qualification.countByExpiresOnLessThan(sixMonths)]
    }

    def list = {
        if (!params.sort) params.sort = 'familyName'
        if (!params.order) params.order = 'asc'
        params.cache = true

        def listQuery = "from Person p where eligibleParent = true and address is not null order by p.${params.sort} ${params.order}"

        if(params?.format && params.format != "html") {
            def l = Person.findAll(
                    "from Person p where eligibleParent = true and address is not null order by p.${params.sort} ${params.order}",
                    [], params)

            def teamList = Team.list([sort: 'ageGroup'])

            // export
            List fields = [
                    "familyName", "givenName", "dateOfBirth", "email", "phone1", "phone2", "address", "fanNumber", "teams", "qualifications"
            ]
            Map labels = [
                    "familyName": "Surname",
                    "givenName": "Names",
                    "dateOfBirth": "DoB",
                    "email": "Email",
                    "phone1": "Phone",
                    "phone2": "Alt Phone",
                    "address": "Address",
                    "fanNumber": "FAN",
                    "teams": "Teams",
                    "qualifications": "Qualifications"
            ]

            def qualFormatter = { member, value ->
                member.qualifications.join("\r\n")
            }

            def teamFormatter = { member, value ->
                def isCoachOrManagerIn = []
                teamList.each { t ->
                    if (t.manager == member || t.coaches.contains(member)) {
                        isCoachOrManagerIn << t
                    }
                }
                isCoachOrManagerIn.join("\r\n")
            }

            def dobFormatter = { member, value ->
                g.formatDate([date: value, format: "yyyy-MM-dd"])
            }

            Map formatters = [
                    qualifications: qualFormatter,
                    teams: teamFormatter,
                    dateOfBirth: dobFormatter
            ]
            Map parameters = [title: "All Members"]

            response.contentType = grailsApplication.config.grails.mime.types[params.format]
            response.setHeader(
                "Content-disposition",
                "attachment; filename=${URLEncoder.encode('member-list','UTF-8')}.${params.extension}"
            )
            exportService.export(
                params.format,
                response.outputStream,
                l,
                fields,
                labels,
                formatters,
                parameters
            )
        }
        else {
            params.max = Math.min(params.max ? params.int('max') : 100, 200)
            def l = Person.findAll(listQuery, [], params)
            def c = Person.countByEligibleParentAndAddressIsNotNull(true)
            [personInstanceList: l, personInstanceTotal: c]
        }
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
     * security for editing is also via the ACL (see FootySecurityService)
     */
    @Secured(["ROLE_COACH"])
    def edit = {
        def personInstance = Person.get(params.id)
        if (!personInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])}"
            redirect(action: "list")
        }
        else
            return [personCommand: personInstance]
    }

    @Secured(["ROLE_COACH"])
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
        try {
            personService.deletePerson(params.id as long)
            flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'person.label', default: 'Person'), params.id])}"
        }
        catch (Exception e) {
            log.error "Unable to delete person with id ${params.id}: $e"
            flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'person.label', default: 'Person'), params.id])}"
        }
        redirect([action: "list"])
    }

    /*
     * User based stuff
     */
    @Secured(["ROLE_SYSADMIN"])
    def editLogin = {
        def person = Person.get(params.id)
        if (!person) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'User'), params.id])}"
            redirect(session.breadcrumb ? [uri: session.breadcrumb] : [action: "listLogins"])
        }
        else {
            def user = person.user ?: new SecUser(enabled: true, username: person.fullName.toLowerCase().replace(' ',''))

            String authorityFieldName = SpringSecurityUtils.securityConfig.authority.nameField
            String authoritiesPropertyName = SpringSecurityUtils.securityConfig.userLookup.authoritiesPropertyName
            List roles = SecRole.list().sort { it.authority }
            Set userRoleNames = []
            if (user.id) {
                userRoleNames = user[authoritiesPropertyName].collect { it[authorityFieldName] }
            }
            def granted = [:]
            def notGranted = [:]
            for (role in roles) {
                String authority = role[authorityFieldName]
                if (userRoleNames.contains(authority)) {
                    granted[(role)] = userRoleNames.contains(authority)
                } else {
                    notGranted[(role)] = userRoleNames.contains(authority)
                }
            }

            render(view: 'editLogin', model: [personCommand: person, userCommand: user, roleMap: granted + notGranted])
        }
    }

    @Secured(["ROLE_SYSADMIN"])
    def saveLogin = {
        try {
            personService.updateLogin(params)
            flash.message = "${message(code: 'default.updated.message', args: [message(code: 'person.label', default: ''), params.id])}"
        }
        catch (Exception ex) {
            log.error "Exception updating login for person with id ${params.id}: $ex"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'User'), params.id])}"
        }

        redirect(session.breadcrumb ? [uri: session.breadcrumb] : [action: "listLogins"])
    }

    def listLogins = {
        params.max = Math.min(params.max ? params.int('max') : 100, 200)
        if (!params.sort) params.sort = 'familyName'
        if (!params.order) params.order = 'asc'
        params.cache = true

        def l = Person.findAllByUserIsNotNull(params)
        [personInstanceList: l, personInstanceTotal: Person.countByUserIsNotNull()]
    }

    def toggleLock = {
        personService.toggleAccountLock(params.id as long)
        redirect(session.breadcrumb ? [uri: session.breadcrumb] : [action: "listLogins"])
    }

    def assignQualification = {
        [qualificationTypes: QualificationType.listOrderByCategory(), personId: params.id]
    }

    def addQualification = {
        Person p
        try {
           p = personService.addQualificationToPerson(params)
        }
        catch (Exception ex) {
            log.error "Unable to add qualification: $ex"
            response.sendError 500, "Error adding qualification"
            return
        }

        // text/plain prevents sitemesh decoreation
        render template: '/person/qualificationsList', plugin: 'footy-core', model: [person: p], contentType: 'text/plain'
    }

    /*
     * URL mapping: /person/delQualification/${personId}/${qualificationId}
     */
    def delQualification = {
        Person p
        try {
            p = personService.deleteQualificationFromPerson(params.personId as long, params.qualificationId as long)
        }
        catch (Exception ex) {
            log.error("Unable to delete qualification: $ex")
        }
        // text/plain prevents sitemesh decoreation
        render template: '/person/qualificationsList', plugin: 'footy-core', model: [person: p], contentType: 'text/plain'
    }

    /**
     * dialog for manager/coach photo to be uploaded
     */
    @Secured(["ROLE_COACH","ROLE_EDITOR"])
    def photoUploadDialog = {
        render (template: 'photoUploadDialog', model: params, contentType: 'text/plain', plugin: 'footy-core')
    }

    /**
     * post action for team photo to be uploaded
     */
    @Secured(["ROLE_COACH","ROLE_EDITOR"])
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
        if (params.teamId) {
            def t = Team.get(params.teamId)
            redirect (controller: 'team', action: 'show', params:[ageBand: t.ageBand, teamName: t.name])
        }
        else
            redirect controller: 'home'
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
