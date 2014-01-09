package org.davisononline.footy.tournament

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.davisononline.footy.core.Person
import org.grails.paypal.*
import grails.plugins.springsecurity.Secured

/**
 * @author darren
 */
@Secured(['ROLE_TOURNAMENT_ADMIN'])
class TournamentController {
    
    // from the export plugin
    def exportService

    def tournamentService


    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    /**
     * redir to list view
     */
    def index = {
        redirect(action: "list", params: params)
    }

    /**
     * sorts by start date, oldest last
     */
    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        if (!params.sort) {
            params.sort = "startDate"
            params.order = "desc"
        } 
        [tournamentInstanceList: Tournament.list(params), tournamentInstanceTotal: Tournament.count()]
    }

    /**
     * create
     */
    def create = {
        def tournamentInstance = new Tournament()
        tournamentInstance.properties = params
        render(view: "edit", model: [tournamentInstance: tournamentInstance])
    }

    /**
     * save
     */
    def save = {
        def tournamentInstance = new Tournament(params)
        if (tournamentInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'tournament.label', default: ''), tournamentInstance.name])}"
            redirect(action: "list")
        }
        else {
            render(view: "edit", model: [tournamentInstance: tournamentInstance])
        }
    }

    /**
     * show tournament details and includes a teamList sorted by age band
     * gathered from all the entries
     */
    def show = {
        def t = checkInstance(params)

        /*
         * export of the current tournament values
         */
        if(params?.format && params.format != "html") {

            def teamList = t.competitions*.entered.flatten()

            List fields = [
                    "competition",
                    "clubAndTeam",
                    "league",
                    "strength",
                    "contact",
                    "contact.email",
                    "contact.phone1",
                    "paymentStatus"
            ]

            Map labels = [
                    "competition": "Competition",
                    "clubAndTeam": "Team",
                    "league": "League",
                    "strength": "Strength",
                    "contact": "Contact",
                    "contact.email": "Email",
                    "contact.phone1": "Tel.",
                    "paymentStatus": "Payment Status"
            ]

            // Formatter closure
            def paymentStatusFmt = { team, value ->
                team?.payment?.status ?: "UNKNOWN"
            }

            def competitionStatusFmt = { team, value ->
                def comp
                t.competitions.each {c->
                    if (c.entered.contains(team)) {
                        comp = c
                        return
                    }
                }
                comp.name
            }

            Map formatters = [competition: competitionStatusFmt, paymentStatus: paymentStatusFmt]
            Map parameters = [title: t.name, "column.widths": [0.1, 0.4, 0.1, 0.1, 0.2, 0.25, 0.2, 0.15]]
        
            response.contentType = ConfigurationHolder.config.grails.mime.types[params.format] 
            response.setHeader(
                "Content-disposition", 
                "attachment; filename=${URLEncoder.encode(t.name,'UTF-8')}-tournament.${params.extension}"
            )            
            exportService.export(
                params.format, 
                response.outputStream, 
                teamList, 
                fields, 
                labels, 
                formatters, 
                parameters
            ) 
        }
        else
            render (view: 'show', model: [tournamentInstance: t])
    }

    /**
     * edit details
     */
    def edit = {
        def t = checkInstance(params)
        render (view: 'edit', model:[tournamentInstance: t])
    }
    
    /**
     * update details
     */
    def update = {
        def tournamentInstance = checkInstance(params)
        if (params.version) {
            def version = params.version.toLong()
            if (tournamentInstance.version > version) {
                
                tournamentInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'tournament.label', default: 'Tournament')] as Object[], "Another user has updated this Tournament while you were editing")
                render(view: "edit", model: [tournamentInstance: tournamentInstance])
                return
            }
        }
        tournamentInstance.properties = params
        if (!tournamentInstance.hasErrors() && tournamentInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.updated.message', args: [message(code: 'tournament.label', default: 'Tournament'), tournamentInstance.name])}"
            redirect(action: "list")
        }
        else {
            render(view: "edit", model: [tournamentInstance: tournamentInstance])
        }
    }

    /**
     * delete tournament
     */
    def delete = {
        def tournamentInstance = checkInstance(params)
        try {
            tournamentInstance.delete(flush: true)
            flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'tournament.label', default: 'Tournament'), tournamentInstance.name])}"
            redirect(action: "list")
        }
        catch (org.springframework.dao.DataIntegrityViolationException e) {
            flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'tournament.label', default: 'Tournament'), params.id])}"
            redirect(action: "show", id: params.id)
        }
    }

    def deleteEntry = {
        def entry = Entry.get(params.entryId)
        def comp = Competition.get(params.compId)
        tournamentService.deleteEntry(comp, entry)
        redirect(controller: "tournament", action: "show", id: params.tournamentId)
    }

    def promoteEntry = {
        tournamentService.moveEntryToEntered(Competition.get(params.compId), Entry.get(params.entryId))
        redirect(controller: "tournament", action: "show", id: params.tournamentId)
    }

    def relegateEntry = {
        tournamentService.moveEntryToWaiting(Competition.get(params.compId), Entry.get(params.entryId))
        redirect(controller: "tournament", action: "show", id: params.tournamentId)
    }

//    @Secured(["ROLE_TOURNAMENT_ADMIN"])
//    def paymentMade = {
//        def e = checkEntry (params)
//        e.payment.status = Payment.COMPLETE
//        e.payment.save()
//        redirect(controller: "tournament", action: "entryList", id: e.tournament.id)
//    }

    /**
     * main application flow
     */
    @Secured(["permitAll"])
    def signupFlow = {

        setup {
            action {
                if (!params.name) {
                    log.error "no tournament name supplied"
                    return notFound()
                }
                def t = Tournament.findByName(params.name)
                if (! t) {
                    log.error "Tournament ${params.name} not found"
                    return notFound()
                }
                if (! t?.openForEntry || t?.competitions?.size() == 0) {
                    log.error "Tournament ${params.name} not setup correctly, or not open for entry"
                    return closed()
                }

                // map of entry:competition
                flow.entries = [:]
                flow.tournament = t
                //flow.competitions = []
                flow.entry = new Entry()
            }
            on(Exception).to("error")
            on("notFound").to("notFound")
            on("closed").to("closed")
            on("success").to("enterEmail")
        }

        enterEmail {
            on("submit") {
                // in the domain class a null email is allowed, we don't want that here
                if (!params.email) params.email = ' '

                // check if person already known or use dummy
                def p = Person.findByEmail(params.email)
                if (!p) p = new Person(email: params.email)

                flow.personInstance = p
                if (!flow.personInstance?.validate(['email']))
                    return error()

            }.to("checkContactDetails")
        }

        checkContactDetails {
            action {
                return (flow.personInstance.validate() ? enterTeamDetails() : enterContactDetails())
            }
            on("enterContactDetails") {
                [personInstance: null, email: params.email]
            }.to "enterContactDetails"
            on("enterTeamDetails").to "enterTeamDetails"

        }

        enterContactDetails {
            on("submit") {
                flow.personInstance = new Person(params)
                if (!flow.personInstance.validate()) {
                    return error()
                }
            }.to "enterTeamDetails"
        }

        enterTeamDetails {
            on("submit") {
                def entry = new Entry(params)
                entry.contact = flow.personInstance
                if (!entry.validate()) {
                    flow.entry = entry
                    return error()
                }

                def competition = Competition.get(params.competition)
                flow.entries[entry] = competition

            }.to "confirmEntry"
        }

        confirmEntry {
            on("createMore") {
                //flow.entry = new Entry()
                [entry: new Entry()]
            }.to "enterTeamDetails"

            on("submit") {
                def payment
                try {
                    payment = tournamentService.createPayment(flow.tournament, flow.entries, flow.personInstance)
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

        closed() {}

        error() {
            render view:'/error'
        }

    }

    private checkInstance(params) {
        def t = Tournament.get(params.id)
        if (!t) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'tournament.label', default: 'Tournament'), params.id])}"
            redirect(action: "list")
        }
        return t
    }
}
