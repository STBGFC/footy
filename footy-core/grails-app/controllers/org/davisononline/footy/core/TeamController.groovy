package org.davisononline.footy.core

import grails.plugins.springsecurity.Secured
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.davisononline.footy.core.utils.ImageUtils

/**
 * controller methods for CRUD on Team
 */
@Secured(["ROLE_CLUB_ADMIN"])
class TeamController {

    def registrationService

    def footySecurityService

    def mailService

    def springSecurityService

    def personService

    def teamService

    def exportService

    def footyMatchService


    static allowedMethods = [save: "POST", update: "POST", delete: "POST", photoUpload: "POST"]


    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        if(params?.format && params.format != "html") {
            // export
            List fields = [
                    "team", "manager", "manager.email", "manager.phone1", "coach", "coachEmail", "coachPhone"
            ]
            Map labels = [
                    "team": "Team",
                    "manager": "Manager",
                    "manager.email": "Manager Email",
                    "manager.phone1": "Manager Phone",
                    "coach": "Coach",
                    "coachEmail": "Coach Email",
                    "coachPhone": "Coach Phone"
            ]

            // Formatter closure
            def teamLabel = { team, value ->
                team
            }
            def coach = { team, value ->
                team.coaches.join("\r\n")
            }
            def coachEmail = { team, value ->
                team.coaches*.email.join("\r\n")
            }
            def coachPhone = { team, value ->
                team.coaches*.phone1.join("\r\n")
            }
            Map formatters = [
                    team: teamLabel,
                    coach: coach,
                    coachEmail: coachEmail,
                    coachPhone: coachPhone
            ]
            Map parameters = [title: "All Teams"]

            response.contentType = ConfigurationHolder.config.grails.mime.types[params.format]
            response.setHeader(
                "Content-disposition",
                "attachment; filename=${URLEncoder.encode('team-list','UTF-8')}.${params.extension}"
            )
            exportService.export(
                params.format,
                response.outputStream,
                Team.list([sort:'ageGroup', order: 'asc']),
                fields,
                labels,
                formatters,
                parameters
            )
        }
        else {
            params.max = Math.min(params.max ? params.int('max') : 25, 100)
            params.sort = params.sort ?: 'ageGroup'
            [teamInstanceList: Team.findAllByClub(Club.homeClub, params), teamInstanceTotal: Team.countByClub(Club.homeClub)]
        }
    }

    @Secured(["permitAll"])
    def show = {
        cache 'authed_page'

        def ag = AgeGroup.findByYear(params.ageBand.toInteger())

        def teamInstance = Team.findWhere(club: Club.homeClub, ageGroup: ag, name:params.teamName)
        if (!teamInstance || teamInstance.club != Club.homeClub) {
            response.sendError(404)
        }
        else {
            def cfg = grailsApplication.config.org?.davisononline?.footy?.match
            def minAge = cfg.minimumagetopublishresults as Integer ?: 11
            def newsAndFixtures = new TreeSet()
            def fixtures = footyMatchService.getFixtures(teamInstance)


            if (teamInstance.ageBand >= minAge) {
                newsAndFixtures.addAll(
                        footyMatchService.getPlayedFixtures(teamInstance, params?.maxNews as int ?: 5)
                )
            }
            newsAndFixtures.addAll(
                    NewsItem.findAllByTeam(teamInstance, [max: params?.maxNews ?: 5, sort:'createdDate', order:'desc'])
            )

            return [
                    teamInstance: teamInstance,
                    players: Player.findAllByTeam(teamInstance, [sort:"person.familyName", order:"asc"]),
                    otherTeamsThisAge: Team.findAllByClubAndAgeGroup(Club.homeClub, teamInstance.ageGroup),
                    newsAndFixtures: newsAndFixtures,
                    fixtures: fixtures
            ]
        }

    }

    @Secured(["permitAll"])
    def addresscards = {
        def teamInstance = Team.get(params?.id)
        if (teamInstance) {
            response.setHeader("Content-disposition", "attachment;filename=${teamInstance.toString().replace(" ", "_")}_contacts.vcf")
            boolean includeParents = footySecurityService.isAuthorisedForManager(teamInstance.id)
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
        render(view: "edit", model: [teamInstance: teamInstance, managers: personService.getManagers()])
    }

    def save = {
        def teamInstance = new Team(params)
        if (teamInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'team.label', default: 'Team'), teamInstance])}"
            redirect(session.breadcrumb ? [uri: session.breadcrumb] : [action: "list"])
        }
        else {
            render(view: "edit", model: [teamInstance: teamInstance, managers: personService.getManagers()])
        }
    }

    def edit = {
        def teamInstance = Team.get(params.id)
        if (!teamInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'team.label', default: 'Team'), params.id])}"
            redirect(session.breadcrumb ? [uri: session.breadcrumb] : [action: "list"])
        }
        else {
            return [teamInstance: teamInstance, managers: personService.getManagers(), players: Player.findAllByTeam(teamInstance, [sort:"person.familyName", order:"asc"])]
        }
    }

    def update = {
        def teamInstance = Team.get(params.id)
        if (teamInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (teamInstance.version > version) {
                    
                    teamInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'team.label', default: 'Team')] as Object[], "Another user has updated this Team while you were editing")
                    render(view: "edit", model: [teamInstance: teamInstance, managers: personService.getManagers()])
                    return
                }
            }
            teamInstance.properties = params
            if (!teamInstance.hasErrors() && teamInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'team.label', default: 'Team'), teamInstance])}"
                redirect(session.breadcrumb ? [uri: session.breadcrumb] : [action: "list"])
            }
            else {
                render(view: "edit", model: [teamInstance: teamInstance, managers: personService.getManagers()])
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
                teamService.deleteTeam(teamInstance)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'team.label', default: 'Team'), params.id])}"
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'team.label', default: 'Team'), params.id])}"
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'team.label', default: 'Team'), params.id])}"
        }

        redirect([action: "list"])
    }

    /**
     * create the dialog for news items
     */
    @Secured(["ROLE_COACH"])
    def newsDialog = {
        render (template: 'newsDialog', model: params, contentType: 'text/plain', plugin: 'footy-core')
    }

    /**
     * adds a news item for display on the team page and optional email to
     * team parents/members
     */
    @Secured(["ROLE_COACH"])
    def addNews = {
        def ni = new NewsItem(params)
        def t = ni.team
        if (ni.save(flush:true)) {
            flash.message = "${message(code: 'org.davisononline.footy.core.newssaved.message', default: 'News saved to page.')}"
            redirect (action: 'show', params:[ageBand: t.ageBand, teamName: t.name])
        }
        else {
            response.setStatus 500
            flash.message = "Failed to save news item"
        }
    }

    /**
     * RSS news feed
     */
    @Secured(["permitAll"])
    def feed = {
        cache "content"
        def t = Team.get(params?.id)
        if (!t) {
            response.sendError 404, 'Unknown feed'
            return
        }

        // render news as RSS
        render(feedType:"rss") {
            title = "${t} News"
            link = "${grailsApplication.config.grails.serverURL}/team/feed/${t.id}"
            description = "Updates for the ${t} team"

            def news = NewsItem.findAllByTeam(t, [max: 25, sort:'createdDate', order:'desc'])
            news.each() { article ->
                entry(article.subject) {
                    author = t.manager
                    publishedDate = article.createdDate
                    link = "${grailsApplication.config.grails.serverURL}/u${t.ageBand}/${t.name}"
                    content(article.body)
                }
            }
        }
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
                response.setHeader 'Content-disposition', "attachment; filename=${teamInstance.ageGroup}-${teamInstance.name}_${teamInstance.league}-registration.pdf".replace(' ', '_')
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
    @Secured(["ROLE_COACH", "ROLE_EDITOR"])
    def photoUploadDialog = {
        render (template: 'photoUploadDialog', model: params, contentType: 'text/plain', plugin: 'footy-core')
    }

    /**
     * post action for team photo to be uploaded
     */
    @Secured(["ROLE_COACH", "ROLE_EDITOR"])
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
        renderImage t?.photo
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

    /**
     * edit or add the sponsor information
     */
    @Secured(["ROLE_COACH", "ROLE_EDITOR"])
    def selectSponsorDialog = {
        params['teamInstance'] = Team.get(params.id)
        render (template: 'selectSponsorDialog', model: params, contentType: 'text/plain', plugin: 'footy-core')
    }

    /**
     * assign a pre-created sponsor to the team
     */
    @Secured(["ROLE_COACH", "ROLE_EDITOR"])
    def assignSponsor = {
        def t = Team.get(params.id)
        if (!t) {
            response.sendError 404, "No such team"
            return
        }
        else {
            t.properties = params
            t.save()
            redirect (action: 'show', params:[ageBand: t.ageBand, teamName: t.name])
        }
    }

    /**
     * send email to various members
     */
    @Secured(["ROLE_COACH"])
    def messageDialog = {
        if (params.id) {
            def t = Team.get(params.id)
            params.ageBand = t.ageBand
            params.defaultTeamId = t.id
        }

        def teams = Team.findAllByClub(Club.homeClub, [sort:'ageGroup', order:'asc'])
        def ageGroups = teams*.ageGroup.unique()

        render (template: 'messageDialog', model: [ageGroups:ageGroups], contentType: 'text/plain', plugin: 'footy-core')
    }

    /**
     * AJAX helper for getting teams in an ageband
     */
    @Secured(["ROLE_COACH"])
    def teamsForAgeBand = {
        if (params.ageBand == '0') {
            render (text: 'Email will go to ALL TEAMS in the database.  Select an age group above to narrow your distribution', contentType: 'text/plain')
            return
        }
        else {
            def ag = AgeGroup.get(params.ageBand)
            def teams = Team.findAllByClubAndAgeGroup(Club.homeClub, ag, [sort:'division', order:'asc'])
            // really nasty kludge.. because the & between params in the remote function call gets URL encoded
            // it prefixes the parameter name with "amp;"
            def defaultId = params["amp;defaultTeamId"] ?: teams[0].id
            render (template: 'teamCheckBoxes', model: [teams:teams,defaultId:defaultId.toLong()], contentType: 'text/plain', plugin: 'footy-core')
        }
    }

    /**
     * sends an email message to the people requested
     */
    @Secured(["ROLE_COACH"])
    def sendMessage = { EmailCommand cmd ->
        def teams
        if (params.ageBand == '0')
            teams = Team.findAllByClub(Club.homeClub)
        else {
            // params.teams might be single value or a list
            teams = Team.findAllByIdInList(cmd.chkTeam.toList())
        }

        def recipients = []
        teams.each { team ->
            recipients << team.manager.email
            if (cmd.to > 0) // coaches
                recipients << team.coaches.collect {it.email}
            if (cmd.to > 1) // everyone
                team.players.each { p ->
                    recipients << p.guardian?.email
                    if (p.secondGuardian) recipients << p.secondGuardian.email
                }
        }
        // tidy up, remove any null/blank emails (shouldn't be any, but..)
        recipients = recipients.flatten().unique() - null - ''

        if (recipients.size() > 0) {
            // do it!
            def user = springSecurityService.currentUser
            def person = Person.findByUser(user)
            mailService.sendMail {
                to person.email
                bcc recipients
                from person.email
                subject cmd.subject
                body cmd.body
            }

            flash.message = "Email sent to ${recipients.size()} people"
        }
        else {
            flash.message = "Email not sent - no recipients found."
        }

        redirect(session.breadcrumb ? [uri: session.breadcrumb] : [uri: '/'])

    }
    
}


/**
 * for send email conversions
 */
class EmailCommand {
    int ageBand
    long[] chkTeam
    int to
    String subject
    String body
}
