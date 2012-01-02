package org.davisononline.footy.core

/**
 * transactional service code for the Person controller and related
 * clients.
 */
class PersonService {

    static transactional = true

    def getManagers() {
        getPeopleWithQualType(QualificationType.COACHING)
    }

    def getReferees() {
        getPeopleWithQualType(QualificationType.REFEREEING)
    }

    def addQualificationToPerson(qual, person) {
        log.debug "Adding [$qual] to [$person]..."

        // remove expiring qualifications of the same type
        def old = person.qualifications?.find {it.type == qual.type}
        old?.each {
            log.debug "Removing old qualification [$it] from [$person]"
            person.removeFromQualifications(it)
            it.delete()
        }

        // add new, save
        person.addToQualifications(qual)
        log.debug "Quals list now [${person.qualifications}]"
        person.save(flush:true)
    }
        
        

    private getPeopleWithQualType(qualType) {
        Person.executeQuery(
                "select distinct q.person from Qualification q where q.type.category=:category order by q.person.familyName asc",
                [category: qualType])
    }

}
