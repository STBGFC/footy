package org.davisononline.footy.registration

import org.davisononline.footy.core.*


/**
 * PersonController
 *
 * @author Darren Davison
 */
//@Secured(['REGISTRATION_ADMIN'])
class PersonController {
    
    static allowedMethods = [update: 'POST', updateNotes:'POST', save:'POST']
    
    def clubService
    def personService
    

    /**
     * show person for the id passed, or the person in context
     */
    def showPerson = {
        if (params?.id) {
            session.context.personId = params.id
        }
        if (!session.context?.personId) {
            render "Please select a person to view"
        }
        
        def p = personService.getPerson(session.context.personId)
        session.context.colourScheme = p.club.colours
        model:[person: p, club: p.club]
    }

    /**
     * show player for the id passed
     */
    def showPlayer = {
        if (!params?.id) {
            render "Please select a player to view"
        }
        
        def p = personService.getPlayer(params.id)
        session.context.colourScheme = p?.club?.colours
        model:[person: p, club: p.club]
    }
    
    /**
     * add new player to team with supplied id, or in context
     */
    def addPlayer = {
        doAdd(new Player(),params?.id)
    }
    
    /**
     * add new player to team with supplied id, or in context
     */
    def addPerson = {
        doAdd(new Person(),params?.id)
    }
    
    private doAdd(p, id) {
        if (!id)
            p.club = clubService.getClub(session.context.clubId)
        else
            p.club = clubService.getClub(id)
        
        if (session.context.teamId && p.instanceOf(Player))
            p.team = Team.get(session.context.teamId)
        render(view:'edit', model: modelFor(p))
    }

    /**
     * commit new person/player to DB
     */
    def save = {
        if (params.containsKey("dateOfBirth")) {
            // player
            log.debug("saving new player")
            def player = new Player(params)
            player.club = clubService.getClub(session.context.clubId)
            
            if (!player?.validate()) {
                render(view: "edit", model: modelFor(player))
            }
            // parent/guardian not listed?
            else if (!player.guardian || player.guardian.id == -1) {
                // save player in session
                session['newplayer'] = player
                flash.title = "Enter parent/guardian details for player ${player.fullName()}"
                render(view: "edit", model: modelFor(new Person(familyName:player.familyName)))
            }
            else {
                personService.saveOrUpdate(player)
                flash.message = "${message(code: 'person.created.message', args: [player.fullName()])}"
                home()
            }
        }
        
        else {
            // parent/guardian
            def person = new Person(params)
            person.club = clubService.getClub(session.context.clubId)
            def player = session['newplayer']
            
            if (!person.validate() || !updateAddressIfNeeded(person, params)) {
                render(view: "edit", model: modelFor(person))
            }
            else {
                if (player) {
                    personService.save(player, person)
                    flash.message = "${message(code: 'person.created.message', args: [player.fullName()])}"
                    session.removeAttribute('newplayer')
                }
                else {
                    personService.saveOrUpdate(person)
                    flash.message = "${message(code: 'person.created.message', args: [person.fullName()])}"
                }
                home()
            }
        }
    }
    
    /**
     * edit an existing person, player or coach
     */
    def edit = {
        def p = personService.getPerson(params.id)
        if (!p) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])}"
            home()
        }
            
        modelFor(p)
    }
    
    /**
     *
     */
    def update = {
        def p = personService.getPerson(params.id)
        if (p) {
            if (params.version) {
                def version = params.version.toLong()
                if (p.version > version) {
                    p.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'person.label', default: 'Person')] as Object[], "Another user has updated this person while you were editing")
                    render(view: "edit", model: [p: p])
                    return
                }
            }
            
            p.properties = params
            updateAddressIfNeeded(p, params)
            if (personService.saveOrUpdate(p)) {
                flash.message = "${message(code: 'person.updated.message', args: [p])}"
                home()
            }
            else
                render(view: "edit", model: modelFor(p))
        }
        
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])}"
            home()
        }
    }
    
    /**
     * add a new coaching qualification for the coach in question.  Render
     * the updated list.
     *
    def addQualification = {
        def coach = personService.getPerson(params.id)
        if (!coach) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'coach.label', default: 'Coach'), params.id])}"
            home()
        }
        def q = new CoachingQualification()
        bindData(q, params, ['id'])
        personService.addQualification(coach, q)
        render(template:'qualifications', model:[p:coach])
    }
    */
    
    /**
     * quick dialog based update for the notes on a player.  Renders the
     * new notes field back again
     */
    def updateNotes = {
        if (params.id) {
            personService.updateNotes(params.id, params.notes)
            render(params.notes.encodeAsHTML())
        }
        else
            render(view: 'showPlayer')
    }
    
    private updateAddressIfNeeded(p, params) {
        if (params['address.address'] || params['address.postCode']) {
            p.address.properties = params['address']
            return p.address.validate()
        }
        else {
            p.address = null
            return true
        }
    }
    
    private modelFor(playerOrPerson) {
        def model = [p: playerOrPerson]
        if (!playerOrPerson.club)
            playerOrPerson.club = clubService.getClub(session.context.clubId)
        if (playerOrPerson.class == Player.class)
            model.put("guardians", personService.getGuardians(playerOrPerson, clubService.getClub(session.context.clubId)))
            
        model
    }
    
}
