import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import org.grails.paypal.Payment
import org.davisononline.footy.core.NewsItem

class FootyTagLib {

    static namespace="footy"

    def footySecurityService


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
     * render a sponsor logo if one exists
     */
    def sponsorLogo = { attrs ->

        def sp = attrs.sponsor
        if (sp) {
            if (sp.url)
                out << "<a href='${sp.url}' title='${sp.name}'>"
            out << "<img id='sponsorlogo'"
            out << "    src='${createLink(controller:'sponsor', action:'logo', id:sp.id)}'"
            if (!sp.url)
                out << "    title='${sp.name} sponsors our team'"
            out << "    alt='${sp.name}'/>"
            if (sp.url)
                out << "</a>"
        }
        else {
            out << "<!-- no sponsor -->"
        }

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
            doTagError "'team' attribute not found in team photo tag."
        }

        def havePhoto = attrs.team.photo?.size() > 0

        out << "<img class='shadow' id='teamphoto'"
        out << "    src='${havePhoto ? createLink(controller:'team', action:'photo', id:attrs.team.id) : createLinkTo(dir:'images', file:'noteam.png')}'"
        out << "    alt='TeamPhoto'/>"
    }

    /**
     * renders a person photo, if one has been added to the database, or
     * the stock "awaiting photo" image if not.
     *
     * <footy:personPhoto person="${personInstance}"/>
     *     .. might render..
     * <img class="userpic" src="/images/nouser.png" alt="No Picture"/>
     *     .. or ..
     * <img class="userpic" src="/person/photo/1234" alt="Joe Bloggs"/>
     *
     * @attr person REQUIRED the person instance to render the photo for
     */
    def personPhoto = { attrs, body ->

        //checking required fields
        if (!attrs.person) {
            doTagError "'person' attribute not found in team photo tag."
        }

        def havePhoto = attrs.person.photo?.size() > 0

        out << "<img class='userpic'"
        out << "    src='${havePhoto ? createLink(controller:'person', action:'photo', id:attrs.person.id) : createLinkTo(dir:'images', file:'nouser.jpg', plugin:'footy-core')}'"
        out << "    alt='${havePhoto ? attrs.person.toString() : 'Awaiting Photo'}'/>"

        //<img class="userpic" src="${createLinkTo(dir:'images',file:'nouser.jpg',plugin:'footy-core')}" alt="No Picture"/>
    }

    /**
     * renders the body content if the current security principal is
     * also the manager of the team passed in the attrs
     *
     * @attr team REQUIRED the team to verify manager for
     */
    def isManager = { attrs, body ->
        if (checkManager(attrs))
            out << body()
    }

    /**
     * renders the body content if the current security principal is
     * not the manager of the team passed in the attrs.  Opposite of
     * the above.
     *
     * @attr team REQUIRED the team to verify manager for
     */
    def isNotManager = { attrs, body ->
        if (!checkManager(attrs))
            out << body()  
    }

    /**
     * output a payment status icon for a supplied Payment object
     *
     * @attr payment REQUIRED the payment object to show the status for
     */
    def paymentStatus = { attrs, body ->
        def payment = attrs.payment

        if (payment) {
            def cash = (payment.status == Payment.COMPLETE && !payment.paypalTransactionId)
            out << """<a href="${createLink(controller:'invoice', action:'show', id:payment.transactionId)}">
            <img align="middle" title="Payment ${cash ? 'made by Cash/Cheque/Credit Card': payment.status}"
            alt="${payment?.status?.toLowerCase()}"
            src="${resource(dir:'images',file:'payment-' + payment?.status?.toLowerCase() + (cash ? 'b' : '') + '.png', plugin:'footy-core')}"/>
            </a>"""
        }
    }

    /**
     * render a list of team news that has been marked site-wide.
     *
     * @param attrs
     */
    def teamNews ={ attrs ->
        def n = attrs.max ?: 10
        def items = NewsItem.findAllByClubWide(true, [max: n, sort: 'createdDate', order: 'desc'])
        items.each { item ->
            def t = item.team
            out << """<h3>${t}, ${formatDate(date: item.createdDate, format: 'dd MMM')}</h3>
            <p>${item.abstractText().encodeAsHTML()}
            <a href="${createLink(controller:'team', action:'show', params:[ageBand:t.ageBand, teamName: t.name])}">
            <br/>${t} homepage..</a></p>"""
        }
    }

    /**
     * render the league table from the FA's fullTime site.  Note, this has to be
     * pre-generated at the fullTime site in a genius rendition of web services
     * in action.  Not.
     *
     * @param attrs
     */
    def fullTimeLeagueTable = { attrs ->

        def div = attrs?.division
        if (!div || !div?.code) return

        out << """
            <div id="lrep${div.code}">Data loading....<a href="http://thefa.com/FULL-TIME">FULL-TIME Home</a></div>
            <script language="javascript" type="text/javascript">
            var lrcode = '${div.code}'
            </script>
            <script language="Javascript" type="text/javascript" src="http://full-time.thefa.com/client/api/cs1.js"></script>
            <p class="footer">(League table is supplied by the FA's "FullTime" web site. All links will take you to the relevant page on that site)</p>
        """
    }

    private boolean checkManager(attrs) {
        if (!attrs.team) {
            doTagError "'team' attribute not found in tag"
        }

        footySecurityService.isAuthorisedForManager(attrs.team)
    }

    private void doTagError(String message) {
        log.error (message)
        throw new GrailsTagException(message)
    }
}
