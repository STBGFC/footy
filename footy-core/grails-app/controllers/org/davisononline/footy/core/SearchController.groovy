package org.davisononline.footy.core

import grails.plugins.springsecurity.Secured

/**
 * uses the grails searchable plugin service but builds our own
 * controller and views
 */
@Secured(['ROLE_CLUB_ADMIN'])
class SearchController {

    def searchableService

    /**
     * Index page with search form and results
     */
    def index = {
        if (!params.q?.trim()) {
            return [:]
        }
        params.suggestQuery = true
        
        try {
            return [searchResult: searchableService.search(params.q, params)]
        } catch (Exception ex) {
            return [parseException: true]
        }
    }
}
