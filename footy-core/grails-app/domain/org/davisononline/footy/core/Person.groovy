package org.davisononline.footy.core


/**
 * Person
 *
 * @author Darren Davison
 */
class Person implements Comparable, Serializable {
    
    String givenName
    String familyName
    String knownAsName
    String email
    String phone1
    String phone2
    Address address
    
    
    static constraints = {
        familyName(blank: false, size: 2..50)
        givenName(nullable: true, blank: false, size: 2..50)
        knownAsName(nullable: true, blank: true)
        email(nullable: true, email: true, blank: false, unique: true)
        address(nullable: true)
        phone1(nullable: true, blank: false)
        phone2(nullable: true, blank: true)
    }
    
    /**
     * comparable to sort on family name, then given name
     */
    int compareTo(obj) {
        sortedName().compareTo(obj.sortedName())
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
    transient String getFullName() {
        "${givenName ?: ''} ${familyName}".trim()
    }
    
    /**
     * @param name
     */
    transient void setFullName(name) {
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
