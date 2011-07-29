package org.davisononline.footy.core

/**
 * Player
 *
 * @author Darren Davison
 */
class Player implements Comparable, Serializable {

    static searchable = {
        spellCheck "include"
        person component: true
    }

    Person person = new Person()
    Date dateOfBirth
    Person guardian
    Person secondGuardian
    Team team
    Date dateJoinedClub = new Date()
    Registration currentRegistration = null
    String leagueRegistrationNumber
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
                return !(obj.ageAtNextCutoff < Person.MINOR_UNTIL && val == null)
            }
        )
        secondGuardian(nullable: true)
        dateOfBirth(nullable:false)
        ethnicOrigin(nullable: true, blank: true)
        doctor(nullable: true)
        doctorTelephone(nullable: true)
        medical(blank: false)
        team(nullable:true)
        currentRegistration(nullable: true)
        leagueRegistrationNumber(nullable: true, unique: true)
        sibling(nullable: true)
        notes(blank: true)
    }

    static mapping = {
        medical type: 'text'
        notes type: 'text'
        person cascade: 'all', lazy: false
        guardian cascade: 'save-update'
        secondGuardian cascade: 'save-update'
        currentRegistration: 'all-delete-orphan'
    }

    /**
     *
     * @param dob
     * @return
     */
    static getAgeAtNextCutoff(Date dob) {
        if (!dob)
            return -1
        def now = new Date()
        def y = now.year
        if (now.month > 7) y++

        if (dob.month > 7)
            return y-1-dob.year
        else
            return y-dob.year
    }

    def getAgeAtNextCutoff() {
        getAgeAtNextCutoff(dateOfBirth)
    }

    def isMinor() {
        return (getAgeAtNextCutoff() < Person.MINOR_UNTIL)
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    String toString() {
        person?.toString()
    }

    int compareTo(t) {
        try {
            person.compareTo(t.person)
        }
        catch (Exception ex) {
            log.error ex
            return 1
        }
    }
}
