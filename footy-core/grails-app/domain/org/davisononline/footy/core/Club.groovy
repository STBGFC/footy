package org.davisononline.footy.core

import org.codehaus.groovy.grails.commons.ConfigurationHolder

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

    static String homeClubName = ConfigurationHolder.config?.org?.davisononline?.footy?.core?.homeclubname

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

    /**
     * static method to return the instance of the Club marked as the
     * 'home' club in the application configuration
     *
     * @return the home Club
     */
    static Club getHomeClub() {
        if (!homeClubName)
            throw new IllegalStateException("Home club name not specified in config: please add 'org.davisononline.footy.core.homeclubname=XXX' to configuration")

        Club.findByName(homeClubName)
    }

    public String toString() {
        name
    }
}

