package org.davisononline.footy.core

import grails.plugins.springsecurity.Secured
import org.grails.paypal.Payment
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.grails.paypal.PaymentItem
import org.springframework.validation.BeanPropertyBindingResult

@Secured(['ROLE_CLUB_ADMIN'])
class PlayerController {

    static allowedMethods = [save: "POST", update: "POST"]

    def playerService

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
        Player.withTransaction {
            if (params?.format && params.format != "html") {
                List fields = [
                        "person.familyName", "person.givenName", "dateOfBirth", "team", "guardian", "guardian.phone1",
                        "guardian.email", "secondGuardian", "secondGuardian.phone1",
                        "secondGuardian.email", "dateJoinedClub", "currentRegistration",
                        "paymentStatus", "ethnicOrigin", "doctor", "doctorTelephone", "medical"
                ]
                Map labels = [
                        "person.familyName"    : "Last Name",
                        "person.givenName"     : "First Name(s)",
                        "dateOfBirth"          : "Date of Birth",
                        "team"                 : "Team",
                        "guardian"             : "Parent/Guardian",
                        "guardian.phone1"      : "Phone",
                        "guardian.email"       : "email",
                        "secondGuardian"       : "Parent/Guardian",
                        "secondGuardian.phone1": "Phone",
                        "secondGuardian.email" : "email",
                        "dateJoinedClub"       : "Date Joined",
                        "currentRegistration"  : "Last Registered",
                        "paymentStatus"        : "Payment Status",
                        "ethnicOrigin"         : "Ethnic Origin",
                        "doctor"               : "Doctor",
                        "doctorTelephone"      : "Doctor's Tel.",
                        "medical"              : "Medical Notes"
                ]

                // Formatter closure
                def formattedDate = { player, value ->
                    value?.format('dd/MM/yy')
                }
                def payment = { player, value ->
                    PaymentItem.findByItemNumber(player?.currentRegistration?.id)?.payment?.status
                }

                Map formatters = [
                        dateOfBirth   : formattedDate,
                        dateJoinedClub: formattedDate,
                        paymentStatus : payment
                ]
                Map parameters = [title: "Current Players (${new Date().format('dd/MM/yyyy')})"]

                response.contentType = ConfigurationHolder.config.grails.mime.types[params.format]
                response.setHeader(
                        "Content-disposition",
                        "attachment; filename=${URLEncoder.encode('player-list', 'UTF-8')}.${params.extension}"
                )
                exportService.export(
                        params.format,
                        response.outputStream,
                        Player.list([sort: 'team', order: 'asc']),
                        fields,
                        labels,
                        formatters,
                        parameters
                )
            } else {
                // standard list
                params.max = Math.min(params.max ? params.int('max') : 25, 100)
                if (!params.sort) params.sort = 'person.familyName'
                def l = Player.list(params)
                [playerInstanceList: l, playerInstanceTotal: Player.count()]
            }
        }
    }

    @Secured(["ROLE_COACH"]) // <-- TEMP
    def edit = {
        log.debug "Edit request for player with id ${params.id}"
        Player.withTransaction {
            def playerInstance = Player.get(params.id)
            if (!playerInstance) {
                flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'player.label', default: 'Player'), params.id])}"
                redirect(action: "list")
            } else {
                return modelForPlayerEdit(playerInstance)
            }
        }
    }

    private modelForPlayerEdit(playerInstance) {

        /*
         * use only valid teams.  Difficult to calculate this accurately (which should normally
         * be currentAge or currentAge plus 1) because team age bands might be rolled forward
         * before the ageband cutoff for the league.  We have to allow current age to current
         * age plus 2 for safety.
         */
        def age = playerInstance.getAgeAtNextCutoff()
        def upperAge = (age < 6) ? 6 : age + 2
        log.debug "Age range for eligible teams is $age to $upperAge"

        def vt = Team.findAll(
                "from Team as t where t.club = :club and t.ageGroup.year >= :age and t.ageGroup.year <= :upper " +
                "order by t.ageGroup.year asc",
                [club: Club.homeClub, age: age, upper: upperAge])
        def parents = Person.findAllByEligibleParentAndAddressIsNotNull(true, [sort:'familyName'])
        def siblings = Player.findAll("from Player p where dateOfBirth <= ? order by p.person.familyName", [playerInstance.dateOfBirth])

        return [playerInstance: playerInstance, validTeams: vt, parents: parents, siblings: siblings]
    }

    @Secured(["ROLE_COACH"]) // <-- TEMP
    def update = {
        Player.withTransaction {
            def playerInstance = Player.get(params.id)
            if (playerInstance) {
                if (params.version) {
                    def version = params.version.toLong()
                    if (playerInstance.version > version) {

                        playerInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'player.label', default: 'Player')] as Object[], "Another user has updated this Player while you were editing")
                        render(view: "edit", model: modelForPlayerEdit(playerInstance))
                        return
                    }
                }

                playerInstance.properties = params
                log.debug playerInstance
                if (!playerInstance.hasErrors() && !playerInstance?.person?.hasErrors() && playerService.updatePlayer(playerInstance)) {
                    flash.message = "${message(code: 'default.updated.message', args: [message(code: 'player.label', default: ''), playerInstance])}"
                    redirect(session.breadcrumb ? [uri: session.breadcrumb] : [action: "list"])
                } else {
                    render(view: "edit", model: modelForPlayerEdit(playerInstance))
                }
            } else {
                flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'player.label', default: 'Player'), params.id])}"
                redirect(session.breadcrumb ? [uri: session.breadcrumb] : [action: "list"])
            }
        }
    }

    def delete = {
        Player.withTransaction {
            def playerInstance = Player.get(params.id)
            if (playerInstance) {
                try {
                    // also remove person record if no qualifications (ie ref or coach)
                    playerService.deletePlayer(playerInstance, (playerInstance.person.qualifications?.size() == 0))
                    flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'player.label', default: 'Player'), params.id])}"
                }
                catch (org.springframework.dao.DataIntegrityViolationException e) {
                    flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'player.label', default: 'Player'), params.id])}"
                }
            }
            else {
                flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'player.label', default: 'Player'), params.id])}"
            }
        }
        redirect([action: "list"])
    }
}
