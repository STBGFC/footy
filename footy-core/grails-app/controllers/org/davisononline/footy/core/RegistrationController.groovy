package org.davisononline.footy.core

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils


/**
 * flows for the registration and creation of players, parents and other
 * staff at the club.
 * 
 * @author darren
 */
class RegistrationController {

    def registrationService

    def springSecurityService

    
    def index = {
        redirect (action:'register')
    }

    /**
     * registration of new players, and/or one or more existing players following a rollover
     * of team age bands (and archival of ex-U18 players)
     */
    def registerFlow = {

        checkOpen {
            def tiers = []
            action {
                tiers = RegistrationTier.findAllByEnabledAndValidUntilGreaterThan(true, new Date());
                if (tiers.size() > 0) {
                    return ok()
                }
                else
                    return closed()
            }

            on ("ok") {
                [tiers: tiers, person: new Person()]
            }.to "start"

            on ("closed") {
                [endMessage:"Registration is currently closed, please check with the club and try again when open."]
            }.to "end"
        }

        start {
            on("continue") {
                flow.registrantEmail = params.email?.toLowerCase()
                def p = Person.findByEmail(flow.registrantEmail)
                if (p) {
                    flow.registrant = p
                } else {
                    flow.person = new Person(params)
                    if (!flow.person.email || !(flow.person.validate(["email"]))) {
                        return error()
                    }
                }

                flow.token = UUID.randomUUID().toString()[0..17]

            }.to "checkTokenRequired"
        }

        checkTokenRequired() {
            action {
                // send mail if not logged in or not a club admin
                if (SpringSecurityUtils.ifNotGranted("ROLE_CLUB_ADMIN")) {
                    registrationService.sendTokenByEmail(flow.registrantEmail, flow.token)
                    log.info "Token ${flow.token} sent by email to ${flow.registrantEmail}"
                    return tokenRequired()
                } else {
                    log.info "Bypassing token sending for registration - ${springSecurityService.currentUser} is logged in as CLUB ADMIN"
                    return tokenNotRequired()
                }

            }

            on ("tokenRequired").to "gatherToken"
            on ("tokenNotRequired").to "checkCanSelectPlayers"
        }

        gatherToken() {
            on ("continue") {
                if (params.token != flow.token) return error()
            }.to "checkCanSelectPlayers"
        }

        checkCanSelectPlayers() {
            action {
                if (flow.registrant == null) {
                    return verifyFirstTime()
                }
                else {
                    return selectPlayer()
                }
            }

            on ("selectPlayer") {
                // if the registrant is transient, (s)he won't have kids in the DB and the findAll would fail
                if (flow.registrant.id) {
                    flow.playersAvailable = Player.findAllByGuardianOrSecondGuardian(flow.registrant, flow.registrant)
                }
                else {
                    flow.playersAvailable = []
                }
                if (flow.addedPlayer) flow.playersAvailable << flow.addedPlayer

                [tiers: RegistrationTier.findAllByEnabledAndValidUntilGreaterThan(true, new Date())]

            }.to "selectPlayer"

            on ("verifyFirstTime").to "verifyFirstTime"

        }

        /*
         * ask for confirmation that you really,really REALLY aren't registered
         */
        verifyFirstTime() {
            on ("continue") {
                // cope with back button better..
                flow.personCommand = new Person (
                    email: flow.registrantEmail
                )
            }.to "enterGuardianDetails"

            on ("end") {
                [endMessage: "Registration process has ended, please use hit the back button in your browser and enter the email address that we currently hold for you in the database in order to continue."]
            }.to "end"
        }

        /*
         * add parent/guardian details and assign to player
         */
        enterGuardianDetails {
            def errors

            on ("addanother") {Person person ->
                person.address = person.address ?: new Address()

                // override any change to email address. Use the validated one
                person.email = flow.registrantEmail

                flow.personCommand = person
                if (!person.address?.validate() | !person.validate()) {
                    flow.address = person.address // see earlier to do item about grails bug
                    return error()
                }
                flow.registrant = person
                flow.personCommand = new Person(familyName: person.familyName, address: person.address)

            }.to "enterSecondGuardianDetails"

            on ("continue") { Person person ->
                person.address = person.address ?: new Address()

                // override any change to email address. Use the validated one
                person.email = flow.registrantEmail

                flow.personCommand = person
                if (!person.address?.validate() | !person.validate()) {
                    flow.address = person.address // see earlier to do item about grails bug
                    return error()
                }
                flow.registrant = person

            }.to "selectPlayer"
        }

        /*
         * add another parent/guardian details and assign to player
         */
        enterSecondGuardianDetails {
            render(view:"enterGuardianDetails")

            on ("continue") { Person person ->
                person.address = person.address ?: new Address()
                person.email = person.email ?: ''

                flow.personCommand = person
                def invalid = (!person.address?.validate() | !person.validate())

                /*
                 * if the same email is used as the first parent, the unique
                 * validation won't trigger at this point - needs an explicit
                 * check
                 */
                if (person.email == flow.registrant?.email) {
                    person.errors.rejectValue('email', 'org.davisononline.footy.core.parentemailduplication.message', 'Cannot use the same email for both parents')
                }

                if (invalid || person.hasErrors()) {
                    flow.address = person.address // see earlier to do item about grails bug
                    return error()
                }

                flow.guardian2 = person

            }.to "selectPlayer"
        }

        /*
         * choose existing players and select registration tiers
         */
        selectPlayer {
            on ("addPlayer").to "setupPlayer"

            on ("continue") {
                flow.registrations = []
                flow.registeredPlayers = []

                if (params.regTierId) {
                    def regIds = [params.regTierId].flatten()
                    def playerIds = [params.playerId].flatten()
                    regIds.eachWithIndex { reg, i ->
                        if (reg != 'x') {
                            def player = flow.playersAvailable[i]
                            def tier = RegistrationTier.get(reg)
                            def registration = Registration.createFrom(tier)
                            flow.registrations << registration
                            flow.registeredPlayers << player
                        }
                    }
                }

            }.to "checkRegistration"
        }

        /*
         * create the flow and model items for the player edit screen
         */
        setupPlayer {
            action {
                flow.addedPlayer = new Player(person: new Person(eligibleParent: false, phone1: 'x'))
                flow.addedPlayer.guardian = flow.registrant
                [playerInstance: flow.addedPlayer]
            }
            on ("success").to "enterPlayerDetails"
        }

        /*
         * main form for player details, not including team
         */
        enterPlayerDetails {
            on("submit") {
                def p = flow.addedPlayer
                p.properties = params

                if (!p.validate(["person", "doctor", "doctorTelephone", "medical", "ethnicOrigin", "dateOfBirth"])
                    | !p.person?.validate()) {
                    // odd.. if i don't do this, the errors object is not visible in the view.
                    // TODO: log this in grails JIRA
                    flow.person = p.person
                    return error()
                }

                flow.duplicate = Player.find(
                        "from Player p where p.dateOfBirth = :dob and p.person.familyName = :familyName and p.person.givenName like :givenName",
                        [dob: p.dateOfBirth, familyName: p.person.familyName, givenName: "${p.person.givenName.split()[0]}%"]
                )

            }.to "checkCanSelectPlayers"
        }

        /*
         * validate that some players have been selected for registration and move to confirm or end the flow if not
         */
        checkRegistration {
            def endMessage
            action {
                if (flow.registeredPlayers.size() > 0)
                    return valid()
                else {
                    endMessage = "You have chosen not to register any players at this time, the registration process is now complete and any details entered have NOT been saved."
                    return end()
                }
            }

            on ("end") {
                [endMessage:endMessage]
            }.to "end"
            
            on ("valid").to "confirm"
        }

        confirm {
            on ("no") {
                [endMessage: "You have chosen NOT to accept the terms and conditions, the registration process has ended."]
            }.to "end"

            on ("yes") {
                flow.registeredPlayers.eachWithIndex { p,i ->
                    p.currentRegistration = flow.registrations[i]
                    flow.registrations[i].player = p
                }
                
                // start transaction to create/save domain
                flow.payment = registrationService.createPayment(flow.registrations)

            }.to "invoice"
        }

        invoice {
            redirect (controller: 'invoice', action: 'show', id: flow.payment.transactionId)
        }

        end { /* end flow with a message */ }
    }

}
