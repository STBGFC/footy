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
     * keep-alive.. async calls to keep the session alive may be required from pages
     * that do a lot of client-side processing priot to submitting
     */
    def keepAlive = {
        render text: 'ok', contentType: 'text/plain'
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
        def fixtures = Fixture.findAllByIdInList(params.fixtures.collect{it as Long}, [sort: 'dateTime'])

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

            /*
             * attempt to ascertain whether the resources for a match have
             * been changed from how they were (so that emails can later be
             * sent only to those managers who's fixtures have been amended)
             */
            def prior = []
            prior.addAll fixture.resources
            fixture.resources.clear()
            def pitch = MatchResource.get(params["pitch$id"] as Long)
            if (pitch) fixture.addToResources(pitch)
            def chRm = MatchResource.get(params["chrm$id"] as Long)
            if (chRm) fixture.addToResources(chRm)

            fixture.amendedResources = (!prior.containsAll(fixture.resources))
        }

        footyMatchService.saveResourceAllocations(fixtures)

        flash.message = 'Fixtures updated and relevant emails have been sent'
        def date = fixtures.first().dateTime
        redirect action: 'summary', params: [year: (date.year + 1900), month: (date.month + 1), day: date.date]
    }

    /**
     * show the summary view of pitch and other resource allocations
     * s/be accessed via UrlMaping "/pitches/$year/$month/$day"
     */
    @Secured(["ROLE_COACH"])
    def summary = {
        def date
        if (params.year)
            date = new Date("$params.year/$params.month/$params.day")
        else {
            // today, if the day is a Sat/Sun, or the following Sat.
            def c = Calendar.instance
            def dow = c.get(Calendar.DAY_OF_WEEK)
            if (dow != 1 && dow != 7) {
                c.set(Calendar.DAY_OF_WEEK, 7)
            }
            date = c.time
        }
        [fixtures: footyMatchService.getHomeGamesOn(date), date: date]
    }
}
