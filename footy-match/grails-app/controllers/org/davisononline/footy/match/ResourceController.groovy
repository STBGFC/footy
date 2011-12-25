package org.davisononline.footy.match

import grails.plugins.springsecurity.Secured
import org.davisononline.footy.core.Person

/**
 * controller for resource allocations (pitches/refs/changing rooms etc) for the
 * fixture secretary to use.
 */
@Secured(["ROLE_CLUB_ADMIN"])
class ResourceController {

    def footyMatchService

    def personService
    

    def index = {
        render view: 'assign'
    }

    /**
     * when a date is changed, automatically renders the fixtures to be considered
     * for that date.
     */
    def changeDate = {
        def m = [
                fixtures: footyMatchService.getHomeGamesOn(params.date ?: new Date()),
                availableReferees: personService.referees,
                availablePitches: MatchResource.findAllByType(MatchResource.PITCH),
                availableChangingRooms: MatchResource.findAllByType(MatchResource.CHANGING_ROOM)
        ]
        render template: 'fixtures', model: m, contentType: 'text/plain', plugin: 'footy-match'
    }

    /**
     * sends all of the daily fixtures with committed resource allocations
     */
    def commitAllocations = {
        // fixtures to operate on from the hidden id array
        def fixtures = Fixture.findAllByIdInList(params.fixtures.collect{it as Long})

        // update each fixture and send the collection to be saved
        fixtures.each { fixture ->
            def id = fixture.id

            // poss. changed ko time
            def ko = Calendar.instance
            ko.time = fixture.dateTime
            ko.set(Calendar.HOUR_OF_DAY, params["hour$id"] as Integer)
            ko.set(Calendar.MINUTE, params["minute$id"] as Integer)
            fixture.dateTime = ko.time

            fixture.referee = Person.get(params["ref$id"] as Long)

            fixture.resources.clear()
            def pitch = MatchResource.get(params["pitch$id"] as Long)
            if (pitch) fixture.addToResources(pitch)
            def chRm = MatchResource.get(params["chrm$id"] as Long)
            if (chRm) fixture.addToResources(chRm)
        }

        footyMatchService.saveResourceAllocations(fixtures)
        
        render text:"OK"
    }
}
