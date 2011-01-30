package org.davisononline.footy.tournament

import org.davisononline.footy.core.Club;

/**
 * Entry controller supplies the main web flow for the tournament entry
 * process.
 * 
 * @author darren
 */
class EntryController {
    
    def identityService
    

    def index = {
        redirect(action: "apply")
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

    def applyFlow = {
        
        def tournament
        
        setup {
            action {
                flow.entries = []
                flow.sessionKey = identityService.nextLong(tournament)
            }
            on("success").to("selectClub")
        }

        selectClub {
            on("selected") {
                def c = Club.get(params.club.id)
                if (!c) {
                    flash.message = "No such club found!?"
                    return error()
                }
                flow.clubInstance = c
            }.to "createEntry"
            on("createNew") {
                flow.clubInstance = null
            }.to "createClub"
        }

        createClub {
            on("submit") {
                def c = new Club(params)
                flow.clubInstance = c
                if (!c.validate()) return error()
                c.save()
            }.to "createEntry"
        }

        createEntry {
            action {
                log.debug ">>> ${flow.clubInstance}"
                [entryInstance: new Entry(club: flow.clubInstance)]
            }
            on("success").to "enterTeamDetails"
        }

        enterTeamDetails {
            on("submit") {
                def e = new Entry(params)
                e.buyerId = flow.sessionKey
                log.debug "Adding buyerId ${flow.sessionKey} to entry ${e}"
                flow.entryInstance = e
                if (!e.validate()) return error()
                e.save()
                flow.entries << e
            }.to "confirmEntry"
        }

        confirmEntry {
            on("submit") {
                // hack because paypal plugin domain objects
                // are not serializable.. we do the work in
                // the PayPalFilters class instead.
                session.buyerId = flow.sessionKey
            }.to("enterPaymentDetails")

            on("createMore").to("createEntry")
        }

        enterPaymentDetails {

        }
        
    }

}