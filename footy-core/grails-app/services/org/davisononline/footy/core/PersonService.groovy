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


    def getCrbs() {
        Person.executeQuery(
                "select distinct q.person from Qualification q where q.type.name=:name and q.expiresOn>:now order by q.person.familyName asc",
                [name: "CRB", now: new Date()]) // CRB should be created in BootStrap
    }
    
    def deletePerson(long personId) {
        def person = Person.get(personId)
        log.info "Request to delete Person ${person} received"
        if (person.user) {
            log.debug "Deleting user account ${person.user}"
            SecUserSecRole.findAllBySecUser(person.user)*.delete()
            person.user.delete()
        }
        person.delete(flush: true)
    }

    def getManagers() {
        log.debug "Returning a list of people with COACHING qualification"
        getPeopleWithQualType(QualificationType.COACHING)
    }

    def getReferees() {
        log.debug "Returning a list of people with REFEREEING qualification"
        getPeopleWithQualType(QualificationType.REFEREEING)
    }

    def addQualificationToPerson(GrailsParameterMap params) {
        def person = Person.get(params.personId)
        def qual = new Qualification(params)

        log.info "Adding ${qual} to ${person}"

        // remove expiring qualifications of the same type
        def old = person.qualifications?.find {it.type == qual.type}
        old?.each {
            log.debug "Removing old qualification ${it} from ${person}"
            person.removeFromQualifications(it)
            it.delete()
        }

        // add new, save
        person.addToQualifications(qual)
        log.debug "Quals list is now ${person.qualifications}"
        person.save(flush:true)
        return person
    }

    def deleteQualificationFromPerson(long personId, long qualificationId) {
        def p = Person.get(personId)
        Qualification.withTransaction {status ->
            // WHY does this not cascade.. the qualification 'belongsTo' the Person
            def q = Qualification.get(qualificationId)
            log.info "Removing qualification ${q} from Person ${p}"
            p.removeFromQualifications(q)
            q.delete()
        }
        return p
    }

    def updateLogin(GrailsParameterMap params) {

        def person = Person.get(params.id)
        def newUser = false
        def SecUser user

        if (!person.user) {
            // deliberately store plain text password here.. user will activate
            log.info "Creating login details for Person ${person}"
            user = new SecUser(params)
            user.password = 'xxx'
            user.save(flush: true)
            person.user = user
            newUser = true
        }
        else {
            log.info "Updating login details for Person ${person}"
            user = person.user
            user.properties = params
        }

        SecUserSecRole.removeAll(user)
        String upperAuthorityFieldName = GrailsNameUtils.getClassName(
                SpringSecurityUtils.securityConfig.authority.nameField, null)

        for (String key in params.keySet()) {
            if (key.contains('ROLE') && 'on' == params.get(key)) {
                log.debug "  ..adding role ${key}"
                SecUserSecRole.create user, SecRole."findBy$upperAuthorityFieldName"(key), true
            }
        }

        String usernameFieldName = SpringSecurityUtils.securityConfig.userLookup.usernamePropertyName
        userCache.removeUserFromCache user[usernameFieldName]

        if (newUser) {
            try {
                log.info "Sending email to ${person.email} for account creation"
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

    }

    def toggleAccountLock(long personId) {
        def personInstance = Person.get(personId)
        if (personInstance?.user) {
            log.info "Toggling account lock status for Person ${personInstance} to ${!personInstance.user.accountLocked}"
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
