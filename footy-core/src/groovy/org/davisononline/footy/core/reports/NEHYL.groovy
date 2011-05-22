package org.davisononline.footy.core.reports

import com.lowagie.text.pdf.BaseFont
import com.lowagie.text.pdf.PdfReader
import com.lowagie.text.pdf.PdfStamper
import com.lowagie.text.Rectangle
import com.lowagie.text.pdf.PdfContentByte
import org.davisononline.footy.core.Player

/**
 * PDF output for NEHYL
 */
class NEHYL {

    def team
    def out

    def createPdf() {

        def DOB_FORMAT = "   dd         MM           yyyy"
        def left = 120
        def content
        def players = Player.findAllByTeam(team, [sort:"person.familyName", order:"asc"])
        def template = org.davisononline.footy.core.RegistrationService.class.getResourceAsStream("NEHYLPlayerRegForm-1.pdf")

        PdfReader pdfReader = new PdfReader(template)
        PdfStamper pdfStamper = new PdfStamper(pdfReader, out)
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false)
        BaseFont bf_symbol = BaseFont.createFont(BaseFont.ZAPFDINGBATS, "Cp1252", false);
        Date today = new Date()

        players.eachWithIndex { player, i ->
            def page = i+1
            if (page > 1) {
                content.endText()
                pdfStamper.insertPage(page, new Rectangle(0, 0))
                pdfStamper.replacePage(pdfReader, 1, page)
            }

            content = pdfStamper.getOverContent(page)
            content.beginText()

            // team
            content.setFontAndSize(bf, 10)
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, "${team.ageBand}", 300, 737, 0)
            def GB = team.girlsTeam ? 478 : 542
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, "X", GB, 737, 0)
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, team.club.name, left-10, 704, 0)
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, team.name, 375, 704, 0)

            // player
            def r = 0
            def sh = 662
            def rh = 19
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, player.person.familyName, left, sh + (r * rh), 0)
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, player.leagueRegistrationNumber, 485, sh + (r-- * rh), 0)
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, player.person.givenName, left, sh + (r-- * rh), 0)
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, player.dateOfBirth.format(DOB_FORMAT), left, sh + (r-- * rh), 0)
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, "${player.guardian.address.house} ${player.guardian.address.address}", left, sh + (r-- * rh), 0)
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, player.guardian.address.town, left, sh + (r-- * rh), 0)
            r--
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, player.guardian.address.postCode, left, sh + (r-- * rh), 0)

            // dates
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, today.format("dd / MM / yyyy"), 455, 321, 0)
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, today.format("dd / MM / yyyy"), 475, 221, 0)
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, today.format("dd / MM / yyyy"), 475, 193, 0)
            // tick marks
            content.setFontAndSize(bf_symbol, 12)
            def sh2 = 444
            6.times {
                content.showTextAligned(PdfContentByte.ALIGN_LEFT, "4", 540, sh2 - (it * 20), 0)
            }
        }

        if (content) content.endText()
        pdfStamper.close()


    }
}
