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
        if (pluginManager.hasGrailsPlugin(FOOTY_CMS))
            //redirect uri: '/content/index'
            forward controller: 'wcmContent', action: 'show'

        render view: '/index'
    }

    /**
     * fetches home page news items as a JSON list
     */
    def news = {
        def items = NewsItem.findAllByClubWide(true, [max: params?.max ?: 8, sort:'createdDate', order:'desc'])
        items
    }
}
