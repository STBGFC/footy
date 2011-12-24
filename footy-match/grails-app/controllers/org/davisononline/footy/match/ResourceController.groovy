package org.davisononline.footy.match

import grails.plugins.springsecurity.Secured

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

}
