

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
}
