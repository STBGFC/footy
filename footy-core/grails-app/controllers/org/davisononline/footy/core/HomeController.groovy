package org.davisononline.footy.core


/**
 * figures out the best home page to display.  UrlMappings should set this
 * controller against the '/' uri
 */
class HomeController {

    static final String CMS = "weceem"

    def pluginManager

    def springSecurityService

    /**
     * default action
     */
    def index = {
        if (pluginManager.hasGrailsPlugin(CMS))
            //redirect uri: '/content/index'
            forward controller: 'wcmContent', action: 'show'

        render view: '/index'
    }

    /**
     * fetches home page news items as an RSS feed.  s/be mapped to a '/feed'
     * url
     */
    def feed = {
        cache "content"

        // render news as RSS
        render(feedType:"rss") {
            title = "${Club.homeClub.name} News Feed"
            link = "${grailsApplication.config.grails.serverURL}/feed"
            description = "Club and team news updates"

            def news = NewsItem.findAllByClubWide(true, [max: 50, sort:'createdDate', order:'desc'])
            news.each() { article ->
                def t = article.team
                entry("${article.subject} (${t})") {
                    author = t.manager
                    publishedDate = article.createdDate
                    link = "${grailsApplication.config.grails.serverURL}/u${t.ageBand}/${t.name}"
                    content(article.body)
                }
            }
        }
    }
}
