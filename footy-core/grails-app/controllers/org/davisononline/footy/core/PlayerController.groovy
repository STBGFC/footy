package org.davisononline.footy.core

import grails.plugins.springsecurity.Secured
import org.grails.paypal.Payment
import org.codehaus.groovy.grails.commons.ConfigurationHolder

@Secured(['ROLE_CLUB_ADMIN'])
class PlayerController {

    static allowedMethods = [save: "POST", update: "POST"]

    def exportService

    /**
     * default action redirect to list
     */
    def index = {
        redirect(action: "list", params: params)
    }

    /**
     * show standard web list or an export of all players in desired format
     */
    def list = {
        if(params?.format && params.format != "html"){
            List fields = ["name", "dateOfBirth", "team", "guardian", "guardian.phone1", "guardian.email", "secondGuardian", "secondGuardian.phone1", "secondGuardian.email", "dateJoinedClub", "currentRegistration", "doctor", "doctorTelephone", "medical"]
            Map labels = [
                    "name": "Name",
                    "dateOfBirth": "Date of Birth",
                    "team": "Team",
                    "guardian": "Parent/Guardian",
                    "guardian.phone1": "Phone",
                    "guardian.email": "email",
                    "secondGuardian": "Parent/Guardian",
                    "secondGuardian.phone1": "Phone",
                    "secondGuardian.email": "email",
                    "dateJoinedClub": "Date Joined",
                    "currentRegistration": "Last Registered",
                    "doctor": "Doctor",
                    "doctorTelephone": "Doctor's Tel.",
                    "medical": "Medical Notes"
            ]

            // Formatter closure
            def name = { player, value ->
                return "${player.toString()}"
            }
            def formattedDate = { player, value ->
                value?.format('dd/MM/yyyy')
            }

            Map formatters = [
                    name: name,
                    dateOfBirth: formattedDate,
                    dateJoinedClub: formattedDate
            ]
            Map parameters = [title: "Current Players (${new Date().format('dd/MM/yyyy')})"]

            response.contentType = ConfigurationHolder.config.grails.mime.types[params.format]
            response.setHeader(
                "Content-disposition",
                "attachment; filename=${URLEncoder.encode('player-list','UTF-8')}.${params.extension}"
            )
            exportService.export(
                params.format,
                response.outputStream,
                Player.list([sort:'team', order: 'asc']),
                fields,
                labels,
                formatters,
                parameters
            )
        }
        else {
            // standard list
            params.max = Math.min(params.max ? params.int('max') : 25, 100)
            if (!params.sort) params.sort = 'person.familyName'
            def l = Player.list(params)
            [playerInstanceList: l, playerInstanceTotal: Player.count()]
        }
    }

    def edit = {
        def playerInstance = Player.get(params.id)
        if (!playerInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'player.label', default: 'Player'), params.id])}"
            redirect(action: "list")
        }
        else {
            /*
             * use only valid teams.  Difficult to calculate this accurately (which should normally
             * be currentAge or currentAge plus 1) because team age bands might be rolled forward
             * before the ageband cutoff for the league.  We have to allow current age to current
             * age plus 2 for safety.
             */
            def age = playerInstance.getAgeAtNextCutoff()
            def upperAge = (age < 7) ? 6 : age + 2
            def vt = Team.findAllByClubAndAgeBandBetween(Club.getHomeClub(), age, upperAge)
            def parents = Person.findAllByEligibleParent(true, [sort:'familyName'])
            return [playerInstance: playerInstance, validTeams: vt, parents: parents]
        }
    }

    def update = {
        def playerInstance = Player.get(params.id)
        if (playerInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (playerInstance.version > version) {

                    playerInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'player.label', default: 'Player')] as Object[], "Another user has updated this Player while you were editing")
                    render(view: "edit", model: [playerInstance: playerInstance])
                    return
                }
            }

            playerInstance.properties = params

            if (!playerInstance.hasErrors() && !playerInstance?.person?.hasErrors() && playerInstance.save()) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'player.label', default: ''), playerInstance])}"
                redirect(action: "list")
            }
            else {
                render(view: "edit", model: [playerInstance: playerInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'player.label', default: 'Player'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def playerInstance = Player.get(params.id)
        if (playerInstance) {
            try {
                playerInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'player.label', default: 'Player'), params.id])}"
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'player.label', default: 'Player'), params.id])}"
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'player.label', default: 'Player'), params.id])}"
        }
        redirect(action: "list")
    }
}
