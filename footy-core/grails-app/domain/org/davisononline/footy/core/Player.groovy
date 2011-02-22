package org.davisononline.footy.core

/**
 * Player
 *
 * @author Darren Davison
 */
class Player {

    Person person
    Date dateOfBirth
    String notes = ''
    Person guardian
    Person secondGuardian
    Team team
    Date dateJoinedClub = new Date()
    String leagueRegistrationNumber = ''

    /*
     * guardian should be nullable:true if the player is >=16.
     * If guardian is null, email should be mandatory, else
     * not.
     */
    static constraints = {
        person(nullable: false)
        guardian(nullable: true,
            validator: { val, obj ->
                // TODO: make age cutoff configurable
				return !(obj.team?.ageBand < 16 && val == null)
			}
        )
        secondGuardian(nullable: true)
        notes(blank: true)
        dateOfBirth(nullable:false)
        team(nullable:true)
    }

    static mapping = { notes type: 'text' }
    
    /**
     * @see java.lang.Object#toString()
     */
    String toString() {
        person.toString()
    }
}
