package org.davisononline.footy.core

/**
 * service that can return trivial identity values of various types
 *
 * @author DD
 */
class IdentityService {

	static transactional = false

	def seqKeys = [:]


	/**
	 * return a globally unique String
	 */
	def guid() {
		UUID.randomUUID().toString()
	}

	/** 
	 * return a sequential long value (will return duplicates after a restart
	 * and therefore not suitable for DB identifiers!
	 *
	 * @param key return the next long for this
	 * sequence name/object
	 */
	synchronized Long nextLong(key) {
		if (!seqKeys.containsKey(key)) {
			seqKeys[key] = 0
		}
		seqKeys[key] += 1
		seqKeys[key]
	}
}
