package org.davisononline.footy.core.reports

import com.lowagie.text.pdf.BaseFont
import com.lowagie.text.pdf.PdfReader
import com.lowagie.text.pdf.PdfStamper
import com.lowagie.text.Rectangle
import com.lowagie.text.pdf.PdfContentByte
import org.davisononline.footy.core.Player

/**
 * PDF output for EBYFL
 */
class EBYFL {

    def team
    def out

    def createPdf() {

        def players = Player.findAllByTeam(team, [sort:"person.familyName", order:"asc"])
        def template = org.davisononline.footy.core.RegistrationService.class.getResourceAsStream("ebfa-reg.pdf")

        def teamY = 455
        def txaxis = [103,193,520]
        def p1y = 247
        def ysize = 27
        def xaxis = [70,280,405,495,543]
        def playersPerPage = 6

        PdfReader pdfReader = new PdfReader(template)
        PdfStamper pdfStamper = new PdfStamper(pdfReader, out)
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false)
        Date today = new Date()

        def content
        def i = 0
        players.each { player ->

            if (player.currentRegistration?.inDate()) {
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
                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, team.club.secretary.toString(), 530, 45, 0)
                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, today.format("dd   MM   yyyy"), 715, 45, 0)
                }

                // player
                content.showTextAligned(PdfContentByte.ALIGN_LEFT, "${player.person.givenName} ${player.person.familyName}", xaxis[0], p1y - (pi * ysize), 0)
                content.showTextAligned(PdfContentByte.ALIGN_LEFT, player.dateOfBirth.format("dd/MM/yyyy"), xaxis[1], p1y - (pi * ysize), 0)
                content.showTextAligned(PdfContentByte.ALIGN_LEFT, player.leagueRegistrationNumber, xaxis[2], p1y - (pi * ysize), 0)
                if (player.guardian.address.house.size() > 8) content.setFontAndSize(bf, 6)
                content.showTextAligned(PdfContentByte.ALIGN_LEFT, player.guardian.address.house, xaxis[3], p1y - (pi * ysize), 0)
                content.setFontAndSize(bf, 10)
                content.showTextAligned(PdfContentByte.ALIGN_LEFT, player.guardian.address.postCode, xaxis[4], p1y - (pi * ysize), 0)
                i++
            }
        }

        if (content) content.endText()
        pdfStamper.close()

    }
}
