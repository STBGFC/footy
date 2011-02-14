package org.davisonononline.footy.tournament

import org.davisononline.footy.core.EmailService

class TournamentService {

    static transactional = false
    
    def EmailService

    def sendConfirmEmail(entry) {
        def email = [
            // TODO: change to [entry.contact.email]. Only sending to me for now!,
            to:      ['darren@davisononline.org'],  
            subject: "Tournament Entry Confirmation", 
            text:    """(Automatic email, please do not reply to this address)

Hi ${entry.contact.knownAsName ?: entry.contact.firstName},

Thank you for your entry to the STBGFC tournament.  This email confirms that
the the following teams are entered into the ${entry.tournament.name} 
competition, and that payment has been received.

${entry.teams.join('\n')}

We'll see you there!
STBGFC Tournament Committee.
"""
        ]
            
        emailService.sendEmails([email])
    }
}
