package org.davisononline.footy.core

import grails.util.GrailsNameUtils
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap

/**
 * transactional service code for the Person controller and related
 * clients.
 */
class PersonService {

    static transactional = true

    def userCache

    def mailService


    def deletePerson(long personId) {
        def person = Person.get(personId)
        if (person.user) {
            SecUserSecRole.findAllBySecUser(person.user)*.delete()
            person.user.delete()
        }
        person.delete(flush: true)
    }

    def getManagers() {
        getPeopleWithQualType(QualificationType.COACHING)
    }

    def getReferees() {
        getPeopleWithQualType(QualificationType.REFEREEING)
    }

    def addQualificationToPerson(GrailsParameterMap params) {
        def person = Person.get(params.personId)
        def qual = new Qualification(params)

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
        return person
    }

    def deleteQualificationFromPerson(long personId, long qualificationId) {
        def p = Person.get(personId)
        Qualification.withTransaction {status ->
            // WHY does this not cascade.. the qualification 'belongsTo' the Person
            def q = Qualification.get(qualificationId)
            p.removeFromQualifications(q)
            q.delete()
        }
        return p
    }

    def updateLogin(GrailsParameterMap params) {
        def person = Person.get(params.id)
        if (!person.user) {
            // deliberately store plain text password here.. user will activate
            person.user = new SecUser(enabled: true, password: 'temp')
        }

        SecUser user = person.user
        user.properties = params
        person.user.save(flush: true)
        SecUserSecRole.removeAll(user)
        String upperAuthorityFieldName = GrailsNameUtils.getClassName(
                SpringSecurityUtils.securityConfig.authority.nameField, null)

        for (String key in params.keySet()) {
            if (key.contains('ROLE') && 'on' == params.get(key)) {
                SecUserSecRole.create user, SecRole."findBy$upperAuthorityFieldName"(key), true
            }
        }

        String usernameFieldName = SpringSecurityUtils.securityConfig.userLookup.usernamePropertyName
        userCache.removeUserFromCache user[usernameFieldName]

        try {
            mailService.sendMail {
                // ensure mail address override is set in dev/test in Config.groovy
                to      person.email
                subject "${Club.homeClub.name} Login Setup"
                body    (view: '/email/core/loginSetup',
                         model: [person: person, homeClub: Club.homeClub])
            }
        }
        catch (Exception ex) {
            log.error "Unable to send email after login setup; $ex"
        }

    }

    def toggleAccountLock(long personId) {
        def personInstance = Person.get(personId)
        if (personInstance?.user) {
            personInstance.user.accountLocked = !personInstance.user.accountLocked
            personInstance.user.save()
        }
    }


    private getPeopleWithQualType(qualType) {
        Person.executeQuery(
                "select distinct q.person from Qualification q where q.type.category=:category order by q.person.familyName asc",
                [category: qualType])
    }
}
