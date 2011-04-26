package org.davisononline.footy.core

import com.lowagie.text.pdf.BaseFont
import com.lowagie.text.pdf.PdfReader
import com.lowagie.text.pdf.PdfStamper
import com.lowagie.text.Rectangle
import com.lowagie.text.pdf.PdfContentByte
import org.grails.paypal.Payment
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.grails.paypal.PaymentItem
import org.hibernate.jdbc.Work
import java.sql.Connection

class RegistrationService {

    /* use the hibernate session factory directly to get around a bug (see later comments where used) */
    def sessionFactory

    static transactional = true

    /**
     * save domain objects for registration flow and return generated payment to
     * display on invoice
     * 
     * @param registrations
     * @return
     */
    Payment createPayment(registrations) {

        registrations.each { registration ->
            registration.player.save()
            registration.save()
        }

        def p0 = registrations[0].player
        def payment = new Payment (
            buyerId: p0.guardian?.id  ?: p0.person.id,
            transactionIdPrefix: "REG",
            currency: Currency.getInstance(ConfigurationHolder.config.org?.davisononline?.footy?.registration?.currency ?: "GBP")
        )
        
        // and again to create items..
        registrations.each { registration ->
            def p = registration.player
            def regItem = new PaymentItem (
                    itemName: "${p} ${registration.tier}",
                    itemNumber: "${registration.id}",
                    amount: registration.tier.amount
            )
            if (p.sibling && registration.tier.siblingDiscount != 0) {
                regItem.discountAmount = registration.tier.siblingDiscount
            }
            payment.addToPaymentItems(regItem)
        }

        payment.save(flush:true)

        /*
         * create the join manually because trying to do;
         *     player.guardian.addToPayments(payment)
         * causes an NPE when the tx commits.  see http://jira.grails.org/browse/GRAILS-7471
         */
        sessionFactory.getCurrentSession().doWork(
                new Work() {
                    void execute(Connection conn) {
                        def stmt = conn.createStatement()
                        stmt.execute("INSERT INTO person_payment(person_payments_id, payment_id) VALUES (${p0.guardian.id}, ${payment.id})")
                        stmt.close()
                    }
                }
            )

        payment

    }

    /**
     * send a registration form PDF to the output stream (out) based on the
     * team and template supplied.
     *
     * EBYFL specific
     */
    void generateRegistrationForm(team, out) {

        def players = Player.findAllByTeam(team, [sort:"person.familyName", order:"asc"])
        def template = org.davisononline.footy.core.RegistrationService.class.getResourceAsStream("ebyfl-reg-2011_2012.pdf")

        def teamY = 465
        def txaxis = [103,193,520]
        def p1y = 255
        def ysize = 29
        def xaxis = [80,265,370,455,520]
        def playersPerPage = 6

        PdfReader pdfReader = new PdfReader(template)
        PdfStamper pdfStamper = new PdfStamper(pdfReader, out)
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false)
        Date today = new Date()

        def content
        players.eachWithIndex { player, i ->

            def pg = (int) Math.ceil((i+1) / playersPerPage)
            def pi = i % playersPerPage

            if (pi == 0) {
                if (pg > 1) {
                    content.endText()
                    pdfStamper.insertPage(pg, new Rectangle(0, 0))
                    pdfStamper.replacePage(pdfReader, 1, pg)
                }

                content = pdfStamper.getOverContent(pg)
                content.beginText()

                // team
                content.setFontAndSize(bf, 12)
                content.showTextAligned(PdfContentByte.ALIGN_LEFT, "U${team.ageBand}", txaxis[0], teamY, 0)
                content.showTextAligned(PdfContentByte.ALIGN_LEFT, team.club.name, txaxis[1], teamY, 0)
                content.showTextAligned(PdfContentByte.ALIGN_LEFT, team.name, txaxis[2], teamY, 0)

                // secretary, date
                content.setFontAndSize(bf, 10)
                content.showTextAligned(PdfContentByte.ALIGN_LEFT, team.club.secretary.toString(), 470, 45, 0)
                content.showTextAligned(PdfContentByte.ALIGN_LEFT, today.format("dd   MM   yyyy"), 725, 45, 0)
            }

            // player
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, player.toString(), xaxis[0], p1y - (pi * ysize), 0)
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, player.dateOfBirth.format("dd/MM/yyyy"), xaxis[1], p1y - (pi * ysize), 0)
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, player.leagueRegistrationNumber, xaxis[2], p1y - (pi * ysize), 0)
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, player.guardian.address.house, xaxis[3], p1y - (pi * ysize), 0)
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, player.guardian.address.postCode, xaxis[4], p1y - (pi * ysize), 0)

        }

        if (content) content.endText()
        pdfStamper.close()

    }
}
