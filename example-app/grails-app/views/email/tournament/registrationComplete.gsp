(Automatic email, please do not reply to this address)

Hi ${entry.contact.knownAsName ?: entry.contact.givenName},

Thank you for your entry to the Example FC tournament.  This email confirms that
the the following teams are entered into the ${entry.tournament.name}
competition:

${entry.teams.join(' \\n')}

Your invoice number is ${entry.payment.transactionId} and you can access this
at <g:createLink absolute="true" controller="invoice" action="show" id="${entry.payment.transactionId}"/>

We'll see you there!
Example FC Tournament Committee.
