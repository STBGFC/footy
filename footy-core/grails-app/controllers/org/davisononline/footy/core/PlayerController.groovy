package org.davisononline.footy.core

import grails.plugins.springsecurity.Secured
import org.grails.paypal.Payment

@Secured(['ROLE_CLUB_ADMIN'])
class PlayerController {

    static allowedMethods = [save: "POST", update: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 25, 100)
        if (!params.sort) params.sort = 'person.familyName'
        def l = Player.list(params)
        [playerInstanceList: l, playerInstanceTotal: Player.count()]
    }

    def edit = {
        def playerInstance = Player.get(params.id)
        if (!playerInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'player.label', default: 'Player'), params.id])}"
            redirect(action: "list")
        }
        else {
            // use only valid teams
            def age = playerInstance.getAgeAtNextCutoff()
            def vt = Team.findAllByClubAndAgeBandBetween(Club.getHomeClub(), age, age+1)
            return [playerInstance: playerInstance, validTeams: vt]
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

            if (!playerInstance.hasErrors() && !playerInstance.person.hasErrors() && playerInstance.save(flush: true)) {
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

    /**
     * manually marks a registration payment as having been made (i.e. outside
     * of the PayPal/credit card route)
     */
    def paymentMade = {
        def payment = Payment.findByTransactionId(params.id)
        if (payment) {
            payment.status = Payment.COMPLETE
            payment.save()
            def player = Player.get(payment.buyerId)
            if (player)
                player.lastRegistrationDate = new Date()
        }
        else
            flash.message = "No such payment found with transaction id ${params.id}"

        redirect(action: "list")
    }
}
