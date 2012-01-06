Hi ${fixture.team.manager.knownAsName ?: fixture.team.manager.givenName},

Your game ${fixture} on ${fixture.dateTime.format('dd/MM')} is confirmed with
the following details:

Kick Off   : ${fixture.dateTime.format('HH:mm')} ${fixture.adjustedKickOff ? '** PLEASE NOTE THE CHANGE OF TIME FROM THAT REQUESTED **' : ''}
Pitch/Other: ${fixture.resources.join(', ')}
Referee    : ${fixture.referee ?: '** NOT ASSIGNED **'} ${fixture.referee?.email ?: ''} ${fixture.referee?.bestPhone() ?: ''}

${fixture.referee ? 'The referee has been notified and has your contact details.' : ''}

Kind regards,
Example FC Fixture Secretary.
