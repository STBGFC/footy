package org.davisononline.footy.core

/**
 * Player
 *
 * @author Darren Davison
 */
class Player implements Serializable {

    Person person = new Person()
    Date dateOfBirth
    Person guardian
    Person secondGuardian
    Team team
    Date dateJoinedClub = new Date()
    Date lastRegistrationDate = null
    String leagueRegistrationNumber = ''
    Player sibling // for discount calculation
    String notes = ''
    String ethnicOrigin
    String doctor
    String doctorTelephone
    String medical = ''

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
        dateOfBirth(nullable:false)
        ethnicOrigin(nullable: true, blank: true)
        doctor(blank: false)
        doctorTelephone(blank: false)
        medical(blank: true)
        team(nullable:true)
        lastRegistrationDate(nullable: true)
        sibling(nullable: true)
        notes(blank: true)
    }

    static mapping = {
        medical type: 'text'
        notes type: 'text'
        person cascade: 'all,delete-orphan'
        guardian cascade: 'save-update'
        secondGuardian: cascade: 'save-update'
    }

    def getAge() {
        // TODO: make cutoff date configurable
        // TODO: calculate properly
        if (!dateOfBirth) return -1
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
