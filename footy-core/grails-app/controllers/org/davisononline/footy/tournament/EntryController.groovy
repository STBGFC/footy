package org.davisononline.footy.tournament

import org.davisononline.footy.core.*
import org.grails.paypal.Payment
import org.grails.paypal.PaymentItem
import grails.plugins.springsecurity.Secured

/**
 * Entry controller supplies the main web flow for the tournament entry
 * process.
 * 
 * @author darren
 */
class EntryController {

    def tournamentService
    

    def index = {
        redirect(action:"apply")
    }

    @Secured(["ROLE_TOURNAMENT_ADMIN"])
    def delete = {
        def e = checkEntry(params)
        def tid = e.tournament.id
        e.delete()
        flash.message = "Entry deleted"
        redirect(controller: "tournament", action: "entryList", id: tid)
    }

    @Secured(["ROLE_TOURNAMENT_ADMIN"])
    def deleteTeam = {
        def tournament = Tournament.get(params.tournamentId)
        def team = Team.get(params.teamId)
        tournament?.entries?.each { e ->
            if (e.teams.contains(team)) {
                e.removeFromTeams(team)
                e.save()
            }
        }
        redirect(controller: "tournament", action: "show", id: tournament.id)
    }

    @Secured(["ROLE_TOURNAMENT_ADMIN"])
    def paymentMade = {
        def e = checkEntry (params)
        e.payment.status = Payment.COMPLETE
        e.payment.save()
        redirect(controller: "tournament", action: "entryList", id: e.tournament.id)
    }

    private checkEntry(params) {
        def e = Entry.get(params?.id)
        if (!e) {
            flash.message = "No such Entry!"
            redirect(controller: "tournament", action: "list")
        }
        return e
    }

    /**
     * main application flow
     */
    def applyFlow = {

        // TODO: replace text with message codes and defaults in messages.properties
        
        setup {
            action {
                if (!params.name) {
                    log.error "no tournament name supplied"
                    return notFound()
                }
                def t = Tournament.findByName(params.name)
                if (! t?.openForEntry) {
                    log.error "Tournament not found, or not open for entry" 
                    return notFound()
                }
                flow.entryInstance = new Entry(tournament: t)
            }
            on(Exception).to("error")
            on("notFound").to("notFound")
            on("success").to("enterContactDetails")
        }
        
        enterContactDetails {
            on("submit") {
                // check first if person already known
                def p = Person.findByEmail(params.email)
                if (!p || p.email?.size() < 5) {
                    p = new Person(params)
                    // difficult to force not-null on domain email field, so extra check here
                    if (!p.email) p.email = ''
                    if (!p.validate()) {
                        flow.personInstance = p
                        return error()
                    }
                }

                flow.personInstance = p
                flow.entryInstance.contact = p
                
            }.to("selectClub")
        }

        selectClub {
            on("selected") {
                def c = Club.get(params.club?.id)
                if (!c) {
                    flash.message = "No such club found!?"
                    return error()
                }
                flow.clubInstance = c
            }.to "checkTeams"
        
            on("createNew") {
                flow.clubInstance = null
            }.to "createClub"
        }

        createClub {
            on("submit") { ClubCommand clubCommand ->
                flow.clubCommand = clubCommand
                if (!clubCommand.validate()) 
                    return error()
                
                // check first if person already known
                def secr = Person.findByEmail(clubCommand.clubSecretaryEmail)
                if (!secr) {
                    log.debug "Person with email [${clubCommand.clubSecretaryEmail}] not known - creating"
                    secr = new Person(
                            eligibleParent: false,
                            address: clubCommand.clubSecretaryAddress,
                            phone1: clubCommand.clubSecretaryPhone
                    )
                    secr.fullName = clubCommand.clubSecretaryName
                    if (!secr.validate()) {
                        flow.clubCommand.errors = secr.errors
                        log.error "Failed to validate club: ${secr.errors}"
                        return error()
                    }
                }
                
                def c = new Club(
                    name: clubCommand.name,
                    colours: clubCommand.colours,
                    secretary: secr
                )
                // unique constraints etc
                if (!c.validate()) {
                    flow.clubCommand.errors = c.errors
                    log.error "Failed to validate club: ${c.errors}"
                    return error()
                }
                flow.clubInstance = c
                flow.newClub = c

            }.to "createTeam"  // no teams to select if club has just been created
        }
        
        checkTeams {
            action {
                def teamList = Team.findAllByClub(flow.clubInstance, [sort:"ageBand", order:"asc"])
                if (teamList?.size() > 0) {
                    flow.teamList = teamList
                    flow.allEntries = flow.entryInstance.tournament.teamsEntered()
                    yes()
                } 
                else no()
            }
            on("yes").to "selectTeam"
            on("no").to "createTeam"
        }
        
        createTeam {
            action {
                flow.teamInstance = new Team(club: flow.clubInstance)
            }
            on("success").to "enterTeamDetails"
        }
        
        selectTeam {
            on("selected") { RegisterCommand regCmd ->
                if (!regCmd.validate())
                    return createNew()
                
                def teams = Team.getAll(regCmd.teamIds?.toList())
                if (!teams || teams.size() == 0) {
                    flash.message = "No such team found!?"
                    return error()
                }
                teams.each { team -> flow.entryInstance.addToTeams(team) }
            }.to "confirmEntry"
            on("createNew") {
                flow.teamInstance = null
            }.to "enterTeamDetails"            
        }
        
        enterTeamDetails {
            on("submit") {
                def team = new Team(params)
                team.manager = flow.entryInstance.contact
                team.club = team.club ?: flow.clubInstance

                if (!team.validate()) {
                    log.error team.errors
                    flow.teamCommand = team
                    return error()
                }

                flow.entryInstance.addToTeams(team)
                
            }.to "confirmEntry"
        }

        confirmEntry {
            on("back").to("selectTeam")
            
            // if (flow.newClub == null) the following
            // should go back to "selectTeam" instead
            on("createMore").to("createTeam")

            on("removeTeams") { RegisterCommand regCmd ->
                if (regCmd.validate()) {
                    def teams = Team.getAll(regCmd.teamIds?.toList())
                    teams.each {flow.entryInstance.removeFromTeams(it)}
                }
            }.to("confirmEntry")
            
            on("submit") {
                def payment = tournamentService.createPayment(flow.entryInstance)
                try {
                    tournamentService.sendConfirmEmail(flow.entryInstance)
                } catch (Exception ex) {
                    log.error(ex)
                }
                
                [payment:payment]

            }.to("invoice")
        }

        invoice {
            redirect (controller: 'invoice', action: 'show', id: flow.payment.transactionId)
        }

        notFound {
            redirect view: "/404"
        }
        
        error() {
            render view:'/error'
        }
        
    }

}

/**
 * command object for the CreateClub form
 * @author darren
 */
class ClubCommand implements Serializable {
    String name
    String colours
    String clubSecretaryName
    String clubSecretaryEmail
    String clubSecretaryPhone
    Address clubSecretaryAddress = new Address()
    String countyAffiliatedTo
    String countyAffiliationNumber
    
    static constraints = {
        name(blank:false, size:2..50)
        colours(blank:false, size:2..30)
        clubSecretaryName(blank:false, size:5..50)
        clubSecretaryEmail(blank: false, email: true)
        clubSecretaryPhone(blank: false, size: 11..20)
        countyAffiliatedTo(blank:false, size: 2..30)
        countyAffiliationNumber(blank:false, size: 2..30)
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    String toString() {
        "Club Command: [$name, colours:$colours, secname:$clubSecretaryName, addr:$clubSecretaryAddress, county:$countyAffiliatedTo, number:$countyAffiliationNumber]"
    }
}

/**
 * command object for the selectTeams form
 * @author darren
 */
class RegisterCommand implements Serializable {
    int[] teamIds
    
    static constraints = {
        teamIds(minSize: 1)
    }
}
