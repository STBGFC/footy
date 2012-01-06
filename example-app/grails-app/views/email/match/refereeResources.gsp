Hi ${ref.knownAsName ?: ref.givenName},

The following fixtures have been assigned to you as referee on the dates
noted:
<g:each in="${myFixtures}" var="f">
<g:formatDate date="${f.dateTime}" format="HH:mm"/> - ${f}
Manager    : ${f.team.manager} (${f.team.manager.email}) - ${f.team.manager.bestPhone()}
Pitch/Other: ${f.resources.join(', ')}

</g:each>


Please contact the managers directly if you have any problems meeting this
schedule.

Kind regards,
Example FC Fixture Secretary.
