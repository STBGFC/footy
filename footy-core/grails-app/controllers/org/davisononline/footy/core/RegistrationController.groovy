package org.davisononline.footy.core

import org.davisononline.footy.core.*
import org.grails.paypal.Payment
import org.grails.paypal.PaymentItem
import org.codehaus.groovy.grails.commons.ConfigurationHolder

/**
 * flows for the redgistration and creation of players, parents and other
 * staff at the club.
 * 
 * @author darren
 */
class RegistrationController {
    
    def index = {
        redirect (action:'registerPlayer')
    }
    
    /**
     * main web flow for player registration 
     */
    def registerPlayerFlow = {

        /*
         * main form for player details, not including team
         */
        enterPlayerDetails {
            on("submit") { PlayerCommand playerCommand ->
                flow.playerCommand = playerCommand
                if (!playerCommand.validate())
                    return error()
            }.to "checkGuardianNeeded"
        }

        /*
         * if player age < [cutoff] we must have at least one
         * parent/guardian details too
         */
        checkGuardianNeeded {
            action {
                if (flow.playerCommand.age() < Person.MINOR_UNTIL && !flow.playerCommand.parentId)
                    return yes()
                else
                    return no()
            }

            on("yes") {
                flow.personCommand = new PersonCommand(
                    familyName: flow.playerCommand.familyName
                )
            }.to "enterGuardianDetails"

            // TODO: if no guardian required, must get player contact details instead
            on("no") {
                if (flow.playerCommand.parentId)
                    flow.guardian1 = Person.get(flow.playerCommand.parentId)
            }.to "assignTeam"
        }

        /*
         * add parent/guardian details and assign to player
         */
        enterGuardianDetails {
            def errors
            on ("continue") { PersonCommand personCommand ->
                errors = handleGuardian(flow, personCommand)
                if (errors) {
                    personCommand.errors = errors
                    return error()
                }
                flow.personCommand = null

                // TODO: ensure teams are from correct age band and home club only
                flow.availableTeams = Team.findAllByClub(Club.getHomeClub())

            }.to "assignTeam"

            on ("addanother") {PersonCommand personCommand ->
                errors = handleGuardian(flow, personCommand)
                if (errors) {
                    personCommand.errors = errors
                    return error()
                }
                flow.personCommand.email = ''
                flow.personCommand.givenName = ''
            }.to "enterGuardianDetails"
        }

        /*
         * select a team and enter league reg number if available
         */
        assignTeam {
            on ("continue") {
                def team = Team.get(params.teamId)

                // create domain from flow objects
                def player = new Player (
                        dateOfBirth: flow.playerCommand.dob,
                        team: team,
                        dateJoinedClub: new Date(),
                        leagueRegistrationNumber: params.leagueRegistration ?: ''
                )
                player.person = new Person(
                        givenName: flow.playerCommand.givenName,
                        familyName: flow.playerCommand.familyName,
                        knownAsName: flow.playerCommand.knownAsName
                )

                if (flow.guardian1) {
                    flow.guardian1.save(flush:true)
                    player.guardian = flow.guardian1
                }
                if (flow.guardian2) {
                    flow.guardian2.save(flush:true)
                    player.secondGuardian = flow.guardian2
                }

                if (!player.save(flush: true))
                    log.error player.errors

                def payment = new Payment (
                    buyerId: player.id,
                    currency: Currency.getInstance("GBP")
                )
                payment.addToPaymentItems(
                    new PaymentItem (
                        itemName: "${player} Registration",
                        // TODO: re-registration will cause duplicate key
                        itemNumber: "${player.id}",
                        // TODO: change to data driven
                        amount: ConfigurationHolder.config.org.davisononline.footy.registration.annualcost
                    )
                )
                payment.save(flush:true)

                [payment:payment]

            }.to "invoice"
        }

        invoice {
            redirect (controller: 'invoice', action: 'show', id: flow.payment.transactionId, params:[returnController: 'registration'])
        }
    }

    /*
     * manage 1 or 2 guardians
     */
    def handleGuardian(flow, personCommand) {
        flow.personCommand = personCommand
        def person = personCommand.toPerson()
        if (!person.validate())
            return person.errors

        // first or second guardian?
        if (!flow.guardian1)
            flow.guardian1 = person
        else
            flow.guardian2 = person
        return null
    }

    def paypalSuccess = {
        def payment = Payment.findByTransactionId(params.transactionId)
        // update registration date for the player
        def player = Player.get(payment.buyerId)
        if (!player.lastRegistrationDate)
            player.lastRegistrationDate = new Date()
        else {
            def nextYear = player.lastRegistrationDate[YEAR] + 1
            player.lastRegistrationDate.set(year: nextYear)
        }

        render view: '/paypal/success', model:[payment: payment]
    }

    def paypalCancel = {
        render view: '/paypal/cancel'
    }

}
