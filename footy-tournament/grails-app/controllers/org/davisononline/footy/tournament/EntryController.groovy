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
    def list = {
        def entries = Entry.list(params)
        [entryInstanceList: entries, entryInstanceTotal: entries.size()]
    }

    // admin only
    def show = {
        if (!params.id) {
            flash.message = "No such Entry!"
            redirect(action: "list")
        }
        else
            [entryInstance:Entry.get(params.id)]
    }

    // admin only
    def delete = {
        if (!params.id)
            flash.message = "No such Entry!"
        def e = Entry.get(params.id)
        if (!e)
            flash.message = "No such Entry!"
        e.delete()
        flash.message = "Entry deleted"
        redirect(action: "list")
    }

    /**
     * main application flow
     */
    def applyFlow = {
        
        setup {
            action {
                if (!params.id) {
                    flash.message = "No such tournament"
                    oops()
                }
                def t = Tournament.get(params.id)
                if (!t || !t.openForEntry) {
                    flash.message = "Tournament not found, or not open for entry" // TODO: replace with message code
                    oops() 
                }
                flow.tournament = t
                flow.entryInstance = new Entry(tournament: t)
            }
            on("oops") {
                println 'oops'
                render flash.message
            }
            on("success").to("selectClub")
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
                if (!clubCommand.validate()) return error()
                
                // create domain
                def a = Address.parse(clubCommand.clubSecretaryAddress)
                a.save(flush:true)
                def secr = new Person()
                secr.fullName = clubCommand.clubSecretaryName
                secr.address = a
                secr.save(flush:true)
                def c = new Club(
                    name: clubCommand.name,
                    colours: clubCommand.colours,
                    secretary: secr
                )
                // unique constraints etc
                if (!c.validate()) {
                    log.error "Failed to validate club: ${c.errors}"
                    return error()
                }
                flow.clubInstance = c
                c.save()
            }.to "createTeam"  // no teams to select if club has just been created
        }
        
        checkTeams {
            action {
                def teamList = Team.findAllByClub(flow.clubInstance)
                if (teamList?.size() > 0) yes() 
                else no()
            }
            on("yes").to "selectTeam"
            on("no") {
                [teamInstance: new TeamCommand(club: flow.clubInstance)]
            }.to "enterTeamDetails"
        }
        
        createTeam {
            action {
                [teamInstance: new TeamCommand(club: flow.clubInstance)]
            }
            on("success").to "enterTeamDetails"
        }
        
        selectTeam {
            on("selected") {
                def t = Team.get(params.team.id)
                if (!t) {
                    flash.message = "No such team found!?"
                    return error()
                }
                flow.teamInstance = t
            }.to "confirmTeam"
            on("createNew") {
                flow.teamInstance = null
            }.to "enterTeamDetails"            
        }
        
        enterTeamDetails {
            on("submit") {
                def t = new Team(params)
                if (!t.validate()) return error()
                t.save()
                flow.entryInstance.addToTeams t
            }.to "confirmTeam"
        }

        confirmEntry {
            on("submit") {
                // hack because paypal plugin domain objects
                // are not serializable.. we do the work in
                // the PayPalFilters class instead.
                flow.entryInstance.save(flush:true)
                session.buyerId = entry.id
            }.to("enterPaymentDetails")
            
            // if the club was added (not selected) the following
            // should go back to "enterTeamDetails" instead
            on("createMore").to("selectTeam")
        }

        enterPaymentDetails()
        
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
    String clubSecretaryAddress
    String countyAffiliatedTo
    String countyAffiliationNumber
    
    static constraints = {
        name(blank:false, size:2..50)
        colours(blank:false, size:2..30)
        clubSecretaryName(blank:false, size:5..50)
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
    
    Club club
    
    // manager
    String contactName
    String phoneNumber
    String mobileNumber
    String email

    // team
    int ageBand
    boolean girlsTeam = false
    String teamName
    League league
    String division

    static constraints = {
        club(nullable:false)
        ageBand(inList:(7..18).toList())
        teamName(blank:false, unique:['club','ageGroup'])
        league(inList:["East Berks", "North East Hants", "Peter Houseman", "West Surrey", "Surrey Primary", "Other (please state below)"])
        division(blank:false)

        contactName(blank:false)
        phoneNumber(blank:true)
        mobileNumber(blank:false)
        email(email:true,blank:false)
    }

}