import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import org.davisononline.footy.core.Person
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.grails.paypal.Payment

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
        if (!attrs.payment)
            doTagError "Payment not found in attributes"

        def payment = attrs.payment

        def cash = (payment.status == Payment.COMPLETE && !payment.paypalTransactionId)
        out << """<a href="${createLink(controller:'invoice', action:'show', id:payment.transactionId)}">
        <img align="middle" title="Payment ${cash ? 'made by Cash/Cheque/Credit Card': payment.status}"
        alt="${payment?.status?.toLowerCase()}"
        src="${resource(dir:'images',file:'payment-' + payment?.status?.toLowerCase() + (cash ? 'b' : '') + '.png', plugin:'footy-core')}"/>
        </a>"""
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
