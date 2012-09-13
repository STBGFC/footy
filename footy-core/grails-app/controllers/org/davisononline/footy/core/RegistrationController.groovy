package org.davisononline.footy.core


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
                [tiers: tiers]
            }.to "start"

            on ("closed") {
                [endMessage:"Registration is currently closed"]
            }.to "registrationClosed"
        }

        registrationClosed() {}

        start {
            on ("continue") {
                def tier = RegistrationTier.get(params.regTierId)
                flow.registration = Registration.createFrom(tier)

            }.to "setupPlayer"
        }

        setupPlayer {
            action {
                flow.player = new Player(person: new Person(eligibleParent: false, phone1: 'x'))
                [playerInstance: flow.player,parents: Person.findAllByEligibleParent(true, [sort:'familyName'])]
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
                        "from Player p where p.dateOfBirth = :dob and p.person.familyName = :familyName and p.person.givenName like :givenName",
                        [dob: p.dateOfBirth, familyName: p.person.familyName, givenName: "${p.person.givenName.split()[0]}%"]
                )
                if (pe?.currentRegistration) {
                    def pmnt = PaymentItem.findByItemNumber(pe.currentRegistration.id)?.payment
                    if (pmnt) {
                        flow.payment = pmnt
                    }
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
                // cope with back button better..
                flow.personCommand = flow.guardian1 ?: new Person (
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

            on ("addanother") {Person person ->
                person.address = person.address ?: new Address()

                flow.personCommand = person
                if (!person.address?.validate() | !person.validate()) {
                    flow.address = person.address // see earlier to do item about grails bug
                    return error()
                }
                flow.guardian1 = person
                flow.personCommand = new Person(familyName: person.familyName, address: person.address)

            }.to "enterSecondGuardianDetails"

            on ("continue") { Person person ->
                person.address = person.address ?: new Address()
                person.email = person.email ?: ''

                flow.personCommand = person
                if (!person.address?.validate() | !person.validate()) {
                    flow.address = person.address // see earlier to do item about grails bug
                    return error()
                }

                flow.guardian1 = person

            }.to "prepTeam"
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
                if (person.email == flow.guardian1?.email) {
                    person.errors.rejectValue('email', 'org.davisononline.footy.core.parentemailduplication.message', 'Cannot use the same email for both parents')
                }

                if (invalid || person.hasErrors()) {
                    flow.address = person.address // see earlier to do item about grails bug
                    return error()
                }

                flow.guardian2 = person

            }.to "prepTeam"
        }

        /*
         * select a team and enter league reg number if available
         */
        prepTeam {
            action {
                // use only valid teams
                def age = flow.player.getAgeAtNextCutoff()
                def upperAge = (age < 7) ? 6 : age + 2
                def vt = Team.findAllByClubAndAgeBandBetween(Club.getHomeClub(), age, upperAge, [sort:'ageBand'])
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

                if (!player.validate()) return error()

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
            redirect (controller: 'invoice', action: 'show', id: flow.payment.transactionId)
        }
    }

    /**
     * re-registration of one or more existing players following a rollover
     * of team age bands (and archival of ex-U18 players)
     */
    def renewRegistrationFlow = {

        start {
            on ("continue") {
                flow.registrations = flow.registrations ?: []
                flow.registeredPlayers = flow.registeredPlayers ?: []
                def team = Team.get(params.teamId)
                [players:team.players]

            }.to "selectPlayer"
        }

        selectPlayer {
            on ("continue") {
                Player player = Player.get(params.playerId)
                flow.player = player

            }.to "checkRegistration"
        }

        checkRegistration {
            def endMessage
            action {
                if (flow.player.dateOfBirth != params.dateOfBirth) {
                    endMessage = "${message(code:'org.davisononline.footy.core.registration.renewal.wrongdob.text', args:[flow.player], default:'{0}\'s DoB is supplied incorrectly.')}"
                    return end()
                }
                if (flow.player.currentRegistration?.date > new Date()) {
                    endMessage = "${message(code:'org.davisononline.footy.core.registration.renewal.indate.text', args:[flow.player], default:'{0}\'s registration details are already up to date.  No re-registration is required at this time')}"
                    return end()
                }
                else
                    return valid()
            }

            on ("end") {
                [endMessage:endMessage]
            }.to "end"
            
            on ("valid").to "selectTier"
        }

        end { /* end flow with a message */ }

        selectTier {
            on ("continue") {
                def tier = RegistrationTier.get(params.regTierId)
                def registration = Registration.createFrom(tier)
                flow.registrations << registration
                flow.registeredPlayers << flow.player

            }.to "addMore"
        }

        addMore {
            on ("yes").to "start"
            on ("no") {
                flow.registeredPlayers.eachWithIndex { p,i ->
                    p.currentRegistration = flow.registrations[i]
                    flow.registrations[i].player = p
                }
                
                // start transaction to create/save domain
                def payment = registrationService.createPayment(flow.registrations)
                [payment:payment]

            }.to "invoice"
        }

        invoice {
            redirect (controller: 'invoice', action: 'show', id: flow.payment.transactionId)
        }
    }

}
