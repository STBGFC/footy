package org.davisonononline.footy.tournament

class TournamentService {

    static transactional = false
    
    def mailService
    
    def sendConfirmEmail(entry) {
        
        mailService.sendMail {
            // ensure mail address override is set in dev/test in Config.groovy
            to      entry.contact.email
            from    "tournament@stbgfc.co.uk"
            subject "Tournament Entry Confirmation"
            body    """(Automatic email, please do not reply to this address)

Hi ${entry.contact.knownAsName ?: entry.contact.givenName},

Thank you for your entry to the STBGFC tournament.  This email confirms that
the the following teams are entered into the ${entry.tournament.name} 
competition, and that payment has been received.

${entry.teams.join('\n')}

We'll see you there!
STBGFC Tournament Committee.
"""
        }
    }
}
