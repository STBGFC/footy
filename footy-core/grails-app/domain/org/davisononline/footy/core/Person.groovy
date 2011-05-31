package org.davisononline.footy.core

import org.grails.paypal.Payment

/**
 * Person
 *
 * @author Darren Davison
 */
class Person implements Comparable, Serializable {

    static searchable = {
        spellCheck "include"
        address component: true
        givenName boost: 1.5
        knownAsName boost: 1.5
        familyName boost: 2.0
    }

    static int MINOR_UNTIL = 20 // temporarily force everyone to be a minor

    String givenName
    String familyName
    String knownAsName
    String email
    String occupation
    String phone1
    String phone2
    Address address
    Boolean eligibleParent = true
    String notes = ''
    SortedSet qualifications
    Set payments

    // security credential
    SecUser user

    static hasMany = [qualifications: Qualification, payments: Payment]
    static fetchMode = [qualifications: 'eager']

    /*
     * don't allow both phone numbers to be null
     */
    static atLeastOnePhoneValidator = { val, obj ->
        !( (obj.phone1 == null) && (obj.phone2 == null) )
    }
    
    static constraints = {
        familyName(blank: false, size: 2..50)
        givenName(blank: false, size: 2..50)
        knownAsName(nullable: true, blank: true)
        email(nullable: true, email: true, blank: false, unique: true)
        address(nullable: true)
        occupation(nullable: true, blank: true)
        phone1(validator: atLeastOnePhoneValidator, nullable: true, blank: false)
        phone2(validator: atLeastOnePhoneValidator, nullable: true)
        user(nullable: true)
        notes(blank: true)
        qualifications(nullable: true)
    }

    static mapping = {
        notes type: 'text'
        address cascade: "all,delete-orphan"
    }
    
    /**
     * comparable to sort on family name, then given name
     */
    int compareTo(obj) {
        "${familyName}, ${givenName}".compareTo("${obj?.familyName}, ${obj?.givenName}")
    }
    
    /**
     * @return "familyName, givenName"
     */
    String sortedName() {
        "${familyName}, ${givenName}"
    }
    
    /**
     * @return
     */
    def getFullName() {
        "${givenName ?: ''} ${familyName}".trim()
    }
    
    /**
     * @param name
     */
    def setFullName(name) {
        if (!name || name.size() == 0) return
        def parts = name.split()
        familyName = parts[-1]
        if (parts.length > 1)
            givenName = parts[0..parts.length - 2].join(' ')
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        if (knownAsName?.size() > 1)
            "${knownAsName} ${familyName}"
        else
            getFullName()
    }
}
