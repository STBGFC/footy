package org.davisononline.footy.core

import grails.test.*

class QualificationTests extends GrailsUnitTestCase {

    static lev1 = new QualificationType(
            name: 'FA Level 1',
            expires: false,
            category: QualificationType.COACHING
    )
    static eAid = new QualificationType(
            name: 'Emergency Aid',
            expires: true,
            category: QualificationType.OTHER
    )

    protected void setUp() {
        super.setUp()
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
                attainedOn: d1,
                expiresOn: d2
        )
        assertEquals "Emergency Aid (expires: 2014/01/01)", q.toString()
    }

    void testExpiryConstraint() {
        mockForConstraintsTests(Qualification)
        def d1 = new Date('2011/01/01')
        def d2 = new Date('2014/01/01')

        // expiring type
        def q = new Qualification(
                type: eAid,
                attainedOn: d1
        )
        assertFalse q.validate()
        assertEquals "validator", q.errors["expiresOn"]
        q.expiresOn = d2
        assertTrue q.validate()

        // non-expiring type
        q = new Qualification(
                type: lev1,
                attainedOn: d1
        )
        assertTrue q.validate()
    }
}
