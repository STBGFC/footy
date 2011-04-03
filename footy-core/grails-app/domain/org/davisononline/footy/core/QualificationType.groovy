package org.davisononline.footy.core

/**
 * enable new types to be created within an application
 */
class QualificationType implements Serializable {

    static final String COACHING = 'Coaching'
    static final String REFEREEING = 'Refereeing'
    static final String OTHER = 'Other'

    String category = OTHER
    int yearsValidFor = 0 // 0 .. never expires
    String name

    static constraints = {
        category inList: [COACHING, REFEREEING, OTHER]
    }

    public String toString() {
        name
    }
}
