package org.davisononline.footy.core

/**
 * uses the grails searchable plugin service but builds our own
 * controller and views
 */
class SearchController {

    def searchableService

    /**
     * Index page with search form and results
     */
    def index = {
        if (!params.q?.trim()) {
            return [:]
        }
        try {
            return [searchResult: searchableService.search(params.q, params)]
        } catch (Exception ex) {
            return [parseException: true]
        }
    }
}
