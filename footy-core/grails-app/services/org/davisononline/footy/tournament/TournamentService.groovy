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
     * @return the payment
     */
    Payment createPayment(tournament, entries, contact) throws Exception {

        log.info("Creating payment for $entries into $tournament")
        if (!entries || entries.size() == 0) {
            throw new Exception("Invalid data received for tournament entries, cannot create payment")
        }

        if (!contact.id) {
            log.debug("Saving contact details for $contact")
            contact.save(flush:true)
        }

        def payment = new Payment (
            buyerId: contact.id,
            currency: Currency.getInstance(currencyCode),
            transactionIdPrefix: "TRN"
        )

        entries.each { entry ->
            log.debug("Saving entry $entry to db..")
            entry.save(flush:true)
            payment.addToPaymentItems(
                new PaymentItem (
                    itemName: "${entry.clubAndTeam} entry to ${tournament.name}",
                    itemNumber: "${tournament.id}-${entry.id}",
                    amount: tournament.costPerTeam
                )
            )
        }

        log.debug("Saving payment record $payment")
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

    def deleteEntry(comp, entry) {
        if (comp.entered.contains(entry)) {
            comp.removeFromEntered(entry)
        }
        else if (comp.waiting.contains(entry)) {
            comp.removeFromWaiting(entry)
        }

        entry.delete(flush:true)
    }

    def moveEntryToWaiting(comp, entry) {
        if (comp.entered.contains(entry)) {
            comp.removeFromEntered(entry)
            comp.addToWaiting(entry)
        }
    }

    def moveEntryToEntered(comp, entry) {
        if (comp.waiting.contains(entry)) {
            comp.addToEntered(entry)
            comp.removeFromWaiting(entry)
        }
    }
}
