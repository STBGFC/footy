import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException

class FootyTagLib {

    static namespace="footy"

    /**
     * renders a tooltip within the body of a link..
     *
     * <footy:tooltip link="/foo/bar.html" value="Click Here">
     *     this is my tooltip text, visible when I hover
     *     over the link with text 'Click Here'
     * </footy:tooltip>
     *
     * @attr link REQUIRED the href attribute of the anchor
     * @attr value REQUIRED the actual text of the link
     */
    def tooltip = { attrs, body ->
        out << "<a class='tt' href='${attrs.link}'>"
        out << attrs.value
        out << '<span class="tooltip"><span class="top"></span><span class="middle">'
        out << body()
        out << '</span><span class="bottom"></span></span></a>'
    }

    /**
     * renders a team photo, if one has been added to the database, or
     * the stock "awaiting photo" image if not.
     *
     * <footy:teamPhoto team="${teamInstance}"/>
     *     .. might render..
     * <img id="teamphoto" src="/images/noteam.png" alt="Team Photo"/>
     *     .. or ..
     * <img id="teamphoto" src="/team/1234/photo" alt="Team Photo"/>
     *
     * @attr team REQUIRED the team instance to render the photo for
     */
    def teamPhoto = { attrs, body ->

        //checking required fields
        if (!attrs.team) {
            def errorMsg = "'team' attribute not found in team photo tag."
            log.error (errorMsg)
            throw new GrailsTagException(errorMsg)
        }

        def havePhoto = attrs.team.photo?.size() > 0

        out << "<img class='shadow' id='teamphoto'"
        out << "    src='${havePhoto ? createLink(controller:'team', action:'photo', id:attrs.team.id) : createLinkTo(dir:'images', file:'noteam.png')}'"
        out << "    alt='TeamPhoto'/>"
    }
}
