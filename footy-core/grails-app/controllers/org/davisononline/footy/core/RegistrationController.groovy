package org.davisononline.footy.core

import org.davisononline.footy.core.*
import org.grails.paypal.Payment
import org.grails.paypal.PaymentItem
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.springframework.dao.DataIntegrityViolationException

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

        start {
            on ("continue") {
                flow.registrationTier = RegistrationTier.get(params.regTierId)
            }.to "setupPlayer"
        }

        setupPlayer {
            action {
                [playerInstance: new Player(person: new Person(eligibleParent: false))]
            }
            on ("success").to "enterPlayerDetails"
        }

        /*
         * main form for player details, not including team
         */
        enterPlayerDetails {
            on("submit") {
                def player = new Player(params)
                flow.playerInstance = player

                // have to fudge the validation a little.. null parent is ok
                // for now, we haven't asked for those details yet.
                def tempGuardian = false
                if (!player.guardian) {
                    player.guardian = new Person()
                    tempGuardian = true
                }
                if (!player.validate() | !player.person?.validate()) {
                    // odd.. if i don't do this, the errors object is not visible in the view.
                    // TODO: log this in grails JIRA
                    flow.person = player.person
                    return error()
                }

                if (tempGuardian)
                    player.guardian = null
                
            }.to "checkPlayerRegistered"
        }

        /*
         * ensure player not already in DB
         */
        checkPlayerRegistered {
            action {
                def p = flow.playerInstance
                def pe = Player.find(
                        "from Player p where p.dateOfBirth = :dob and p.person.familyName = :familyName and p.person.givenName = :givenName",
                        [dob: p.dateOfBirth, familyName: p.person.familyName, givenName: p.person.givenName]
                )
                if (pe) {
                    flow.payment = Payment.findByBuyerId(pe.id)
                    return yes()
                }
                else
                    return no()
            }

            on("yes").to "invoice"
            on("no").to "checkGuardianNeeded"
        }

        /*
         * if player age < [cutoff] we must have at least one
         * parent/guardian details too
         */
        checkGuardianNeeded {
            action {
                if (flow.playerInstance.isMinor() && !flow.playerInstance.guardian && !flow.playerInstance.secondGuardian)
                    return yes()
                else
                    return no()
            }

            on("yes") {
                flow.personCommand = new Person (
                    familyName: flow.playerInstance.person.familyName
                )
            }.to "enterGuardianDetails"

            // TODO: if no guardian required, must get player contact details instead
            on("no").to "prepTeam"

        }

        /*
         * add parent/guardian details and assign to player
         */
        enterGuardianDetails {
            def errors
            on ("continue") { Person person ->
                flow.personCommand = person
                if (!person.address.validate() || !person.validate()) {
                    flow.address = person.address // see earlier to do item about grails bug
                    return error()
                }
                flow.guardian1 = person

            }.to "prepTeam"

            on ("addanother") {Person person ->
                flow.personCommand = person
                if (!person.address.validate() || !person.validate()) {
                    flow.address = person.address // see earlier to do item about grails bug
                    return error()
                }
                flow.guardian2 = person
                flow.personCommand.email = ''
                flow.personCommand.givenName = ''

            }.to "enterGuardianDetails"
        }

        /*
         * select a team and enter league reg number if available
         */
        prepTeam {
            action {
                // use only valid teams
                def age = flow.playerInstance.getAgeAtNextCutoff()
                def upperAge = (age < 7) ? 6 : age + 1
                def vt = Team.findAllByClubAndAgeBandBetween(Club.getHomeClub(), age, upperAge)
                [validTeams: vt]
            }
            on("success").to("assignTeam")
        }

        assignTeam {
            on ("continue") {

                // create domain from flow objects
                def player = flow.playerInstance
                player.properties = params

                if (flow.guardian1) {
                    player.guardian = flow.guardian1
                }
                if (flow.guardian2) {
                    player.secondGuardian = flow.guardian2
                }

                def payment
                try {
                    Player.withTransaction { status ->
                        if (!player.save(flush: true)) {
                            log.error player.errors
                            status.setRollbackOnly()
                            return error()
                        }

                        payment = new Payment (
                                buyerId: player.id,
                                transactionIdPrefix: "REG",
                                currency: Currency.getInstance(ConfigurationHolder.config.org?.davisononline?.footy?.registration?.currency ?: "GBP")
                        )
                        def regItem =
                        new PaymentItem (
                                itemName: "${player} ${flow.registrationTier}",
                                itemNumber: "${flow.registrationTier.id}",
                                amount: flow.registrationTier.amount
                        )
                        if (player.sibling && flow.registrationTier.siblingDiscount != 0) {
                            regItem.discountAmount = flow.registrationTier.siblingDiscount
                        }
                        payment.addToPaymentItems(regItem)
                        payment.save(flush:true)
                    }
                }
                catch (Exception ex) {
                    return error()
                }

                [payment:payment]

            }.to "invoice"
        }

        invoice {
            redirect (controller: 'invoice', action: 'show', id: flow.payment.transactionId, params:[returnController: 'registration'])
        }
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
