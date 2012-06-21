package org.davisononline.footy.core

/**
 * League
 * 
 * @author darren
 */
class League implements Serializable {

	String name
	Person contact
	SortedSet divisions
    
    static constraints = {
		name size: 2..50, unique: true
		contact nullable: true
    }

    static hasMany = [divisions: Division]
    
    String toString() {
        name
    }
}
