package org.davisononline.footy.core

/**
 * enable new types to be created within an application
 */
class QualificationType {

    static final String COACHING = 'Coaching'
    static final String REFEREEING = 'Refereeing'
    static final String OTHER = 'Other'

    String category = OTHER
    boolean expires = false
    String name

    static constraints = {
        category inList: [COACHING, REFEREEING, OTHER]
    }

    public String toString() {
        name
    }
}
