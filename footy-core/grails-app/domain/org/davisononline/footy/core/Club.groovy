package org.davisononline.footy.core

/**
 * Club
 * 
 * @author Darren Davison
 */
class Club implements Serializable {

    String name
    String website
    Address address
    Person chairman
    Person viceChairman
    Person secretary
    Person treasurer
    Person childProtectionOfficer
    List teams = []
    byte[] logo
    String colours 
    
    static hasMany = [teams: Team]

    static constraints = {
        name(size: 2..50, unique: true)
        website(url: true, nullable: true)
        address(nullable: true)
        chairman(nullable: true)
        viceChairman(nullable: true)
        secretary(nullable: false)
        treasurer(nullable: true)
        childProtectionOfficer(nullable: true)
        logo(nullable: true)
        colours(blank: false)
    }

    public String toString() {
        name
    }
}

