package org.davisononline.footy.core

/**
 * sets a session variable with the request URI for the page about to be
 * forwarded to so that subsequently controllers can use it as a
 * redirect target.  It enables the user to get back to the page they
 * originally clicked an edit link from.
 */
class BreadCrumbsFilters {

    def springSecurityService


    def filters = {
        listShow (controller:'(team|player|person)', action:'(list|listLogins|show|edit)', regex: true) {
            after = {
                if (springSecurityService.currentUser) {
                    def uri = request.forwardURI - request.contextPath
                    if (request.queryString) uri += ('?' + request.queryString)

                    if (!response.committed) {
                        session['breadcrumb'] = uri
                        log.debug "breadcrumb=${session['breadcrumb']}"
                    }
                }
            }
        }
    }
    
}
