package org.davisononline.footy.tournament

import org.grails.paypal.Payment
import org.grails.paypal.PaymentItem
import org.davisononline.footy.tournament.Entry
import org.codehaus.groovy.grails.commons.ConfigurationHolder


class TournamentService {

    static transactional = true

    def mailService

    def CFG = ConfigurationHolder.config?.org?.davisononline?.footy?.tournament

    def fromEmail = CFG?.registration?.email

    def currencyCode = CFG?.currency ?: "GBP"



    /**
     * create and persist Payment for invoice based on tournament entry flow
     *
     * @param entries
     * @param competitions
     * @return the payment
     */
    Payment createPayment(tournament, entries, competitions) {

        if (!entries || !competitions || entries.size() == 0 || entries.size() != competitions.size()) {
            throw new Exception("Invalid data received for tournament entries, cannot create payment")
        }

        def contact = entries[0].contact

        if (!contact.id)
            contact.save(flush:true)

        def payment = new Payment (
            buyerId: contact.id,
            currency: Currency.getInstance(currencyCode),
            transactionIdPrefix: "TRN"
        )

        entries.eachWithIndex { e, i ->
            e.save(flush:true)
            competitions[i].addEntry(e)
            payment.addToPaymentItems(
                new PaymentItem (
                    itemName: "${e.clubAndTeam} entry to ${tournament.name}",
                    itemNumber: "${tournament.id}-${e.id}",
                    amount: tournament.costPerTeam
                )
            )
        }
        payment.save()
        entries.each {
            it.payment = payment
            it.save(flush:true)
        }

        try {
            def cclist = tournament?.cclist?.split(/(;|,)/) ?: ""
            mailService.sendMail {
                // ensure mail address override is set in dev/test in Config.groovy
                to      contact.email
                bcc     cclist
                from    fromEmail
                subject "Tournament Entry Confirmation"
                body    (view: '/email/tournament/registrationComplete',
                         model: [tournament: tournament, entries: entries, contact: contact])
            }
        }
        catch (Exception ex) {
            log.warn "Unable to send email after tournament entry; $ex"
        }

        payment
    }

    def deleteEntry(tournament, entry) {
        tournament?.competitions?.each {c->
            if (c.entered.contains(entry)) {
                c.removeFromEntered(entry)
            }
            else if (c.waiting.contains(entry)) {
                c.removeFromWaiting(entry)
            }
        }
        entry.delete(flush:true)
    }
}
