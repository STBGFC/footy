package org.davisononline.footy.core

import com.lowagie.text.pdf.BaseFont
import com.lowagie.text.pdf.PdfReader
import com.lowagie.text.pdf.PdfStamper
import com.lowagie.text.Rectangle
import com.lowagie.text.pdf.PdfContentByte

class RegistrationService {

    static transactional = false

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

            // players
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, player.toString(), xaxis[0], p1y - (pi * ysize), 0)
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, player.dateOfBirth.format("dd/MM/yyyy"), xaxis[1], p1y - (pi * ysize), 0)
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, player.leagueRegistrationNumber, xaxis[2], p1y - (pi * ysize), 0)
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, player.guardian.address.address.split()[0], xaxis[3], p1y - (pi * ysize), 0)
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, player.guardian.address.postCode, xaxis[4], p1y - (pi * ysize), 0)

        }

        if (content) content.endText()
        pdfStamper.close()

    }
}
