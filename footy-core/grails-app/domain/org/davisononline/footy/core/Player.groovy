package org.davisononline.footy.core

/**
 * Player
 *
 * @author Darren Davison
 */
class Player implements Serializable {

    Person person
    Date dateOfBirth
    String notes = ''
    Person guardian
    Person secondGuardian
    Team team
    Date dateJoinedClub = new Date()
    Date lastRegistrationDate = null
    String leagueRegistrationNumber = ''

    /*
     * guardian should be nullable:true if the player is >= Person.MINOR_UNTIL.
     * If guardian is null, email should be mandatory, else
     * not.
     */
    static constraints = {
        person(nullable: false)
        guardian(nullable: true,
            validator: { val, obj ->
                return !(obj.age < Person.MINOR_UNTIL && val == null)
            }
        )
        secondGuardian(nullable: true)
        notes(blank: true)
        dateOfBirth(nullable:false)
        team(nullable:true)
        lastRegistrationDate(nullable: true)
    }

    static mapping = {
        notes type: 'text'
        person cascade: 'all,delete-orphan'
        guardian cascade: 'save-update'
        secondGuardian: cascade: 'save-update'
    }

    def getAge() {
        // TODO: make cutoff date configurable
        // TODO: calculate properly
        def now = new Date()
        def c = 1900 + (now.month > 7 ? 1 : 0)
        def cutoff = new Date("${now.year+c}/08/31")
        Math.floor((cutoff-dateOfBirth)/365.24)
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    String toString() {
        person.toString()
    }
}
