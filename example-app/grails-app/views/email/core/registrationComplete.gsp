(Automatic email, please do not reply to this address)

Dear ${buyer.givenName},

Thank you for registering at Example FC.  This email confirms that thefollowing
players are registered in the database:

${registrations*.player.join(' \\n')}

Your invoice number is ${payment.transactionId} and you can access this at
<g:createLink absolute="true" controller="invoice" action="show" id="${payment.transactionId}"/>


Kind regards,
Example FC Chairman.

