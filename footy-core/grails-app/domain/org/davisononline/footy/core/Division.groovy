package org.davisononline.footy.core

/**
 * holds common data about a division, for example
 * links and other data to external sites (principally
 * the FA's full-time web site)
 */
class Division implements Comparable, Serializable {

    int ageBand = 9
    String name
    String code // for generating tables, RESTful id etc.
    int index = 0 // lower the number, higher the division
    String standings // for holding cached HTML or other table data

    static belongsTo = [league: League]
    
    static constraints = {
        code nullable:true
        standings nullable: true
        ageBand range: 8..18
        name blank: false, size: 1..30, unique: ['ageBand']
    }

    static mapping = {
        standings type: 'text'
    }

    /**
     * compares by ageBand first, then index
     */
    int compareTo(Object t) {

        if (t?.ageBand != ageBand)
            return ageBand - t?.ageBand

        if (! t?.index)
            return 99

        return (index - t?.index)
    }

    String toString() {
        "${league.toString()} U${ageBand} ${name}"
    }
}
