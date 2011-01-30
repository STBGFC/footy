package org.davisononline.footy.core

import org.davisononline.footy.core.IdentityService;

import grails.test.*

class IdentityServiceTests extends GrailsUnitTestCase {

    def identityService

    protected void setUp() {
        super.setUp()
		identityService = new IdentityService()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testNextLong() {
        assertEquals(1, identityService.nextLong("foo"))
        assertEquals(2, identityService.nextLong("foo"))
        assertEquals(1, identityService.nextLong("bar"))
        assertEquals(1, identityService.nextLong("baz"))
        assertEquals(3, identityService.nextLong("foo"))
    }
	
	void testOneGuid() {
		def guid = identityService.guid()
		assertEquals(36, guid.size())
	}
}
