package org.davisononline.footy.tournament

import org.davisononline.footy.core.*

/**
 * Entry controller supplies the main web flow for the tournament entry
 * process.
 * 
 * @author darren
 */
class EntryController {
    
    def identityService
    

    def index = {
        redirect(action:"apply")
    }
    
    // admin only
    def delete = {
        if (!params.id)
            flash.message = "No such Entry!"
        def e = Entry.get(params.id)
        if (!e) {
            flash.message = "No such Entry!"
            redirect(controller: "tournament", action: "list")            
        }
        else {
            def tid = e.tournament.id
            e.delete()
            flash.message = "Entry deleted"
            redirect(controller: "tournament", action: "entryList", id: tid)
        }
    }

    /**
     * main application flow
     * TODO: replace text with message codes and defaults in messages.properties
     */
    def applyFlow = {
        
        setup {
            // TODO: fix selection of tournament / not open for applications.
            action {
                def t = Tournament.get(1)
                if (!t || !t.openForEntry) {
                    flash.message = "Tournament not found, or not open for entry" // TODO: replace with message code
                    oops() 
                }
                flow.entryInstance = new Entry(tournament: t)
            }
            on("oops").to("error")
            on("success").to("enterContactDetails")
        }
        
        enterContactDetails {
            on("submit") {
                // check first if person already known
                def p = Person.findByEmail(params.email)
                if (!p)
                    p = new Person(params)
                
                flow.personInstance = p
                if (!p.validate())
                    return error()
                
                p.save(flush: true)
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
                    a.save(flush:true)
                    secr.fullName = clubCommand.clubSecretaryName
                    secr.address = a
                    secr.save(flush:true)
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
                    manager: flow.personInstance
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
                // hack because paypal plugin domain objects
                // are not serializable.. we do the work in
                // the PayPalFilters class instead.
                flow.entryInstance.save(flush:true)
                session['org.davisononline.footy.tournament.buyerId'] = flow.entryInstance.id
            }.to("enterPaymentDetails")
        }

        enterPaymentDetails()
        
        error() {
            render (view:'/error')
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
