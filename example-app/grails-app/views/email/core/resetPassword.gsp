(Automatic email, please do not reply to this address)

Hi ${person.knownAsName ?: person.givenName},

We received a request to reset the password for your account at Example FC.  If
you didn't make this request, you can just ignore the email and no changes
will be made to your account.

To continue with the password reset, please click the link below or copy and
paste into your browser.  Beware if the link has wrapped over more than one
line in your email program.

<g:createLink absolute="true" controller="login" action="reset" id="${link}"/>

Please note, this link is only valid for 24 hours.

Kind regards,
Example FC Admin.
