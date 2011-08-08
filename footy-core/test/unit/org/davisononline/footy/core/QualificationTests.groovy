package org.davisononline.footy.core

import grails.test.*

class QualificationTests extends GrailsUnitTestCase {

    static lev1 = new QualificationType(
            name: 'FA Level 1',
            yearsValidFor: 0,
            category: QualificationType.COACHING
    )
    static eAid = new QualificationType(
            name: 'Emergency Aid',
            yearsValidFor: 3,
            category: QualificationType.OTHER
    )

    protected void setUp() {
        super.setUp()
        mockLogging(Qualification.class)
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testToStringNonExpiry() {
        def d1 = new Date('2011/01/01')
        def q = new Qualification(
                type: lev1,
                attainedOn: d1
        )
        assertEquals "FA Level 1 (attained: 2011/01/01)", q.toString()
    }

    void testToStringExpiry() {
        def d1 = new Date('2011/01/01')
        def d2 = new Date('2014/01/01')
        def q = new Qualification(
                type: eAid,
                attainedOn: d1
        )
        assertEquals "Emergency Aid (expires: 2013/12/31)", q.toString()
    }

    void testExpiry() {
        def d1 = new Date('2011/01/01')
        def d2 = new Date('2014/01/01')

        // expiring type
        def q = new Qualification(
                type: eAid,
                attainedOn: d1
        )
        assertEquals d2-1, q.expiresOn

        // non-expiring type
        q = new Qualification(
                type: lev1,
                attainedOn: d1
        )
        assertNull q.expiresOn
    }

    void testCompareToNull() {
        def q = new Qualification(
                type: lev1,
                attainedOn: new Date()
        )
        assertEquals 1, q.compareTo(null)
    }
}
