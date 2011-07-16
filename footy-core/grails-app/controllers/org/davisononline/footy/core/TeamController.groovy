package org.davisononline.footy.core

import grails.plugins.springsecurity.Secured
import org.davisononline.footy.core.utils.ImageUtils


/**
 * controller methods for CRUD on Team
 */
@Secured(["ROLE_CLUB_ADMIN"])
class TeamController {

    def registrationService

    def footySecurityService


    static allowedMethods = [save: "POST", update: "POST", delete: "POST", photoUpload: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 25, 100)
        params.sort = params.sort ?: 'ageBand'
        [teamInstanceList: Team.findAllByClub(Club.homeClub, params), teamInstanceTotal: Team.countByClub(Club.homeClub)]
    }

    @Secured(["permitAll"])
    def show = {
        def teamInstance = Team.findWhere(club: Club.homeClub, ageBand:params.ageBand.toInteger(), name:params.teamName)
        if (!teamInstance || teamInstance.club != Club.homeClub) {
            response.sendError(404)
        }
        else {
            return [teamInstance: teamInstance, players: Player.findAllByTeam(teamInstance, [sort:"person.familyName", order:"asc"])]
        }

    }

    @Secured(["permitAll"])
    def addresscards = {
        def teamInstance = Team.get(params?.id)
        if (teamInstance) {
            response.setHeader("Content-disposition", "attachment;filename=${teamInstance.toString().replace(" ", "_")}_contacts.vcf")
            boolean includeParents = footySecurityService.isAuthorisedForManager(teamInstance)
            def contacts = [teamInstance.manager, teamInstance.coaches, (includeParents ? teamInstance.players*.guardian : [])].flatten()
            render (
                template: '/team/vcard',
                plugin: 'footy-core',
                collection: contacts,
                contentType: 'text/x-vcard'
            )
        }
        else {
            response.sendError(404)
        }
    }

    def create = {
        def teamInstance = new Team()
        teamInstance.properties = params
        render(view: "edit", model: [teamInstance: teamInstance, managers: getManagers()])
    }

    def save = {
        def teamInstance = new Team(params)
        if (teamInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'team.label', default: 'Team'), teamInstance])}"
            redirect(session.breadcrumb ? [uri: session.breadcrumb] : [action: "list"])
        }
        else {
            render(view: "edit", model: [teamInstance: teamInstance, managers: getManagers()])
        }
    }

    def edit = {
        def teamInstance = Team.get(params.id)
        if (!teamInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'team.label', default: 'Team'), params.id])}"
            redirect(session.breadcrumb ? [uri: session.breadcrumb] : [action: "list"])
        }
        else {
            return [teamInstance: teamInstance, managers: getManagers(), players: Player.findAllByTeam(teamInstance, [sort:"person.familyName", order:"asc"])]
        }
    }

    def update = {
        def teamInstance = Team.get(params.id)
        if (teamInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (teamInstance.version > version) {
                    
                    teamInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'team.label', default: 'Team')] as Object[], "Another user has updated this Team while you were editing")
                    render(view: "edit", model: [teamInstance: teamInstance, managers: getManagers()])
                    return
                }
            }
            teamInstance.properties = params
            if (!teamInstance.hasErrors() && teamInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'team.label', default: 'Team'), teamInstance])}"
                redirect(session.breadcrumb ? [uri: session.breadcrumb] : [action: "list"])
            }
            else {
                render(view: "edit", model: [teamInstance: teamInstance, managers: getManagers()])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'team.label', default: 'Team'), params.id])}"
            redirect(session.breadcrumb ? [uri: session.breadcrumb] : [action: "list"])
        }
    }

    def delete = {
        def teamInstance = Team.get(params.id)
        if (teamInstance) {
            try {
                teamInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'team.label', default: 'Team'), params.id])}"
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'team.label', default: 'Team'), params.id])}"
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'team.label', default: 'Team'), params.id])}"
        }

        redirect(session.breadcrumb ? [uri: session.breadcrumb] : [action: "list"])
    }

    /**
     * create the league registration form PDF from the current team
     * and players, sending direct to the ServletOutputStream
     *
     * @return
     */
    @Secured(["ROLE_COACH"])
    def leagueForm = {
        def teamInstance = Team.get(params.id)
        if (teamInstance) {
            try {
                response.contentType = 'application/octet-stream'
                response.setHeader 'Content-disposition', "attachment; filename=U${teamInstance.ageBand}-${teamInstance.name}_${teamInstance.league}-registration.pdf"
                def out = response.outputStream
                registrationService.generateRegistrationForm(teamInstance, out)
                out.flush()
                out.close()
                return null
            }
            catch (Exception e) {
                log.error(e)
                render view: 'error'
            }
        }
    }

    /**
     * dialog for team photo to be uploaded
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
        def t = Team.get(params.id)

        if (params.delete) {
            t.photo = null
            t.save(flush:true)
        }
        else if(!photo.empty) {
            def bytes = ImageUtils.convertImageToByteArray(ImageUtils.resize(photo.fileItem.tempFile, 240, 180), "PNG")
            t.photo = bytes
            t.save(flush:true)
        }
        redirect (action: 'show', params:[ageBand: t.ageBand, teamName: t.name])
    }

    /**
     * render the actual team photo as an image source
     *
     * @return the bytes for the image
     */
    @Secured(["permitAll"])
    def photo = {
        cache "pics"
        def t = Team.get(params.id)
        response.contentType = "image/png"
    	response.contentLength = t?.photo?.length
    	response.outputStream.write(t?.photo)
    }


    private getManagers() {
        Person.executeQuery(
                "select distinct q.person from Qualification q where q.type.category=:category order by q.person.familyName asc",
                [category: QualificationType.COACHING])
    }
}
