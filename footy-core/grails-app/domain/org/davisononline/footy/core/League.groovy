package org.davisononline.footy.core

/**
 * League
 * 
 * @author darren
 */
class League {

	String name
	Person contact
	
    static constraints = {
		name(size: 2..50)
		contact(nullable: true)
    }
}
