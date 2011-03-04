package org.davisononline.footy.tournament

import org.davisononline.footy.core.*
import org.grails.paypal.Payment
import org.grails.paypal.PaymentItem

/**
 * Entry controller supplies the main web flow for the tournament entry
 * process.
 * 
 * @author darren
 */
class EntryController {
    
    def identityService
    def tournamentService
    

    def index = {
        redirect(action:"apply")
    }
    
    // admin only
    def delete = {
        def e = checkEntry(params)
        def tid = e.tournament.id
        e.delete()
        flash.message = "Entry deleted"
        redirect(controller: "tournament", action: "entryList", id: tid)
    }

    // admin only
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
                if (!params.id) {
                    log.error "no tournament id supplied"
                    throw new IllegalStateException("Which tournament did you mean?")
                }
                def t = Tournament.get(params.id)
                if (! t?.openForEntry) {
                    log.error "Tournament not found, or not open for entry" 
                    throw new IllegalStateException("Tournament not found, or not open for entry")
                }
                flow.entryInstance = new Entry(tournament: t)
            }
            on(Exception).to("error")
            on("success").to("enterContactDetails")
        }
        
        enterContactDetails {
            on("submit") {
                // check first if person already known
                def p = Person.findByEmail(params.email)
                if (!p) {
                    p = new Person(params)
                    if (!p.save()) {
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
                    secr = new Person()
                    // create domain
                    def a = Address.parse(clubCommand.clubSecretaryAddress)
                    secr.fullName = clubCommand.clubSecretaryName
                    secr.address = a
                    secr.save(flush: true)
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
                c.save(flush: true)
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
                flow.teamInstance = new TeamCommand(clubId: flow.clubInstance.id)
            }
            on("success").to "enterTeamDetails"
        }
        
        selectTeam {
            on("selected") { RegisterCommand regCmd ->
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
            on("submit") { TeamCommand teamCommand ->
                flow.teamCommand = teamCommand
                if (!teamCommand.validate()) 
                    return error()
                
                def team = new Team(
                    club: flow.clubInstance,
                    league: League.get(teamCommand.leagueId),
                    name: teamCommand.name,
                    division: teamCommand.division,
                    ageBand: teamCommand.ageBand,
                    girlsTeam: teamCommand.girlsTeam,
                    manager: flow.entryInstance.contact
                )
                
                if (!team.validate()) {
                    log.error team.errors
                    teamCommand.errors = team.errors
                    return error()
                }
                team.save(flush: true)
                flow.entryInstance.addToTeams(team)
                
            }.to "confirmEntry"
        }

        confirmEntry {
            on("back").to("enterTeamDetails")
            
            // if (flow.newClub == null) the following
            // should go back to "selectTeam" instead
            on("createMore").to("createTeam")
            
            on("submit") {
                def entry = flow.entryInstance
                def payment = new Payment (
                    buyerId: entry.contact.id,
                    currency: Currency.getInstance("GBP")
                )
                entry.teams.each { t->
                    payment.addToPaymentItems(
                        new PaymentItem (
                            itemName: "${t.club.name} U${t.ageBand} ${t.name}",
                            itemNumber: "${entry.tournament.name}",
                            amount: entry.tournament.costPerTeam
                        )
                    )
                }
                payment.save()
                entry.payment = payment
                entry.save(flush:true)
                [payment:payment]

            }.to("invoice")
        }

        invoice()
        
        error() {
            render (view:'/error')
        }
        
    }

    /**
     * successful paypal payment made
     * @param params
     */
    def paypalSuccess = {
        def payment = Payment.findByTransactionId(params.transactionId)
        if(payment?.status == org.grails.paypal.Payment.COMPLETE) {
            def entry = Entry.findByPayment(payment)
            log.debug("Processed ${entry} for payment")

            if (!entry.emailConfirmationSent) {
                tournamentService.sendConfirmEmail(entry)
                entry.emailConfirmationSent = true
                entry.save()
            }

            render view: 'paypal/success', model:[entry:entry]
        }
        else {
            flash.message = "Unable to find Entry for this transaction"
            render view: "/error"
        }
    }

    /**
     * cancelled transaction
     *
     * @param params
     * @return
     */
    def paypalCancel = {
        render view: "paypal/cancel"
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
    String clubSecretaryAddress
    String countyAffiliatedTo
    String countyAffiliationNumber
    
    static constraints = {
        name(blank:false, size:2..50)
        colours(blank:false, size:2..30)
        clubSecretaryName(blank:false, size:5..50)
        clubSecretaryEmail(blank: false, email: true)
        /*
         * ensure what is submitted can be parsed as some
         * sort of Address
         */
        clubSecretaryAddress(validator: {
            Address.parse(it) != null
        })
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
 * command object for the enterTeamDetails form
 * @author darren
 */
class TeamCommand implements Serializable {
    
    int clubId

    // team
    int ageBand
    boolean girlsTeam = false
    String name
    int leagueId
    String division

    static constraints = {
        ageBand(inList:(7..18).toList())
        name(blank:false)
        division(blank:false)
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
