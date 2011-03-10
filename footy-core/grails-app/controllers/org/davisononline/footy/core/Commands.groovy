package org.davisononline.footy.core

/**
 * @author darren
 * @since 10/03/11
 */

abstract class AbstractPersonCommand implements Serializable {
    String givenName
    String familyName

    static constraints = {
        givenName(nullable:false, blank:false, size:1..50)
        familyName(nullable:false, blank:false, size:1..50)
    }
}

class PlayerCommand extends AbstractPersonCommand {
    Date dob
    Long parentId
    String knownAsName

    static constraints = {
        dob(nullable: false)
        knownAsName(nullable:true, size:1..50)
    }

    /**
     * @return age at cutoff
     */
    int age() {
        // TODO: make cutoff date configurable
        def now = new Date()
        def c = 1900 + (now.month > 7 ? 1 : 0)
        def cutoff = new Date("${now.year+c}/08/31")
        Math.floor((cutoff-dob)/365.24)
    }
}

class PersonCommand extends AbstractPersonCommand {
    String email
    String phone1
    String phone2
    String address

    /**
     *
     * @return a Person domain object (possibly invalid) from the command
     */
    def toPerson() {
        new Person(
                givenName: givenName,
                familyName: familyName,
                email: email,
                phone1: phone1,
                phone2: phone2,
                address: Address.parse(address),
                eligibleParent: true
                )
    }
}
