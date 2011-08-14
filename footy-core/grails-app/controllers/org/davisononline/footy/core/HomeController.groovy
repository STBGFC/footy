package org.davisononline.footy.core

/**
 * figures out the best home page to display.  UrlMappings should set this
 * controller against the '/' uri
 */
class HomeController {

    static final String FOOTY_CMS = "footy-cms"

    def pluginManager

    def springSecurityService

    /**
     * default action
     */
    def index = {

        if (springSecurityService.loggedIn) {
            def user = SecUser.findByUsername(springSecurityService.authentication.name)
            def person = user ? Person.findByUser(user) : null
            def teams = []
            if (person) {
                Team.list().each { t ->
                    if (t.manager == person || t.coaches.contains(person)) teams << t
                }
            }
            render view: '/admin', model: [person:person, teams:teams]
        }
        else
            forward action: 'contentIndex'
    }

    def contentIndex = {
        if (pluginManager.hasGrailsPlugin(FOOTY_CMS))
            redirect uri: '/content/index'

        render view: '/index'
    }
}
