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
    Date dateOfBirth
    Long parentId
    Long secondParentId
    String knownAsName

    static constraints = {
        dateOfBirth(nullable: false)
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
        Math.floor((cutoff-dateOfBirth)/365.24)
    }

    /**
     * @return a Player domain object (possibly invalid) from the command
     */
    def toPlayer() {
        def person = new Person(
                givenName: givenName,
                familyName: familyName,
                knownAsName: knownAsName
        )
        new Player(
                person: person,
                dateJoinedClub: new Date(),
                dateOfBirth: dateOfBirth
        )
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
