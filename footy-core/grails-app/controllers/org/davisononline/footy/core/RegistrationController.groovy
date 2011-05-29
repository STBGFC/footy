package org.davisononline.footy.core

import org.davisononline.footy.core.*
import org.grails.paypal.Payment
import org.grails.paypal.PaymentItem
import org.codehaus.groovy.grails.commons.ConfigurationHolder

/**
 * flows for the registration and creation of players, parents and other
 * staff at the club.
 * 
 * @author darren
 */
class RegistrationController {

    def registrationService
    
    def index = {
        redirect (action:'registerPlayer')
    }
    
    /**
     * main web flow for player registration 
     */
    def registerPlayerFlow = {

        start {
            on ("continue") {
                def tier = RegistrationTier.get(params.regTierId)
                def renewOn = new Date()
                def mth = renewOn[Calendar.MONTH] + tier.monthsValidFor
                renewOn.set(month: mth)
                flow.registration = new Registration(tier: tier, date: renewOn)

            }.to "setupPlayer"
        }

        setupPlayer {
            action {
                flow.player = new Player(person: new Person(eligibleParent: false, phone1: 'x'))
                [playerInstance: flow.player]
            }
            on ("success").to "enterPlayerDetails"
        }

        /*
         * main form for player details, not including team
         */
        enterPlayerDetails {
            on("submit") {
                def player = flow.player
                player.properties = params

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
                def p = flow.player
                def pe = Player.find(
                        "from Player p where p.dateOfBirth = :dob and p.person.familyName = :familyName and p.person.givenName = :givenName",
                        [dob: p.dateOfBirth, familyName: p.person.familyName, givenName: p.person.givenName]
                )
                if (pe?.currentRegistration) {
                    def pmnt = PaymentItem.findByItemNumber(pe.currentRegistration.id)?.payment
                    if (pmnt) {
                        flow.payment = pmnt
                        return invoice()
                    }
                    // registered, but can't find the payment
                    return duplicate()
                }
                else
                    return no()
            }

            on("invoice").to "invoice"
            on("duplicate").to "duplicate"
            on("no").to "checkGuardianNeeded"
        }

        /*
         * if player age < [cutoff] we must have at least one
         * parent/guardian details too
         */
        checkGuardianNeeded {
            action {
                def p = flow.player
                if (p.isMinor() && !p.guardian && !p.secondGuardian)
                    return yes()
                else
                    return no()
            }

            on("yes") {
                flow.personCommand = new Person (
                    familyName: flow.player.person.familyName
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
                if (!person.address) person.address = new Address()
                flow.personCommand = person
                if (!person.address?.validate() | !person.validate()) {
                    flow.address = person.address // see earlier to do item about grails bug
                    return error()
                }
                if (flow.guardian1) flow.guardian2 = person else flow.guardian1 = person

            }.to "prepTeam"

            on ("addanother") {Person person ->
                if (!person.address) person.address = new Address()
                flow.personCommand = person
                if (!person.address?.validate() | !person.validate()) {
                    flow.address = person.address // see earlier to do item about grails bug
                    return error()
                }
                flow.guardian1 = person
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
                def age = flow.player.getAgeAtNextCutoff()
                def upperAge = (age < 7) ? 6 : age + 2
                def vt = Team.findAllByClubAndAgeBandBetween(Club.getHomeClub(), age, upperAge)
                [validTeams: vt]
            }
            on("success").to("assignTeam")
        }

        assignTeam {
            on ("continue") {

                // create domain from flow objects
                Player player = flow.player
                player.properties = params

                if (flow.guardian1) {
                    player.guardian = flow.guardian1
                }
                if (flow.guardian2) {
                    player.secondGuardian = flow.guardian2
                }

                // seems odd..
                player.currentRegistration = flow.registration
                flow.registration.player = player

                // eventually will allow several registrations per flow..
                def registrations = [flow.registration]

                // start transaction to create/save domain
                def payment
                payment = registrationService.createPayment(registrations)

                [payment:payment]

            }.to "invoice"
        }

        duplicate() {}

        invoice {
            redirect (controller: 'invoice', action: 'show', id: flow.payment.transactionId, params:[returnController: 'registration'])
        }
    }

    def paypalSuccess = {
        def payment = Payment.findByTransactionId(params.transactionId)
        render view: '/paypal/success', model:[payment: payment]
    }

    def paypalCancel = {
        render view: '/paypal/cancel'
    }

}
