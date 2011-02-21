package org.davisononline.footy.registration

/**
 * flows for the redgistration and creation of players, parents and other
 * staff at the club.
 * 
 * @author darren
 */
class RegistrationController {

    def personService
    
    def index = { }
    
    /**
     * main web flow for player registration 
     */
    def registerPlayerFlow = {
        
        /*
         * main form for player details, not including team
         */
        enterPlayerDetails {
            on("submit") { PlayerCommand playerCommand ->
                flow.playerCommand = playerCommand
                if (!playerCommand.validate())
                    return error()
            }.to "checkGuardianNeeded"
        }
        
        /*
         * if player age < [cutoff] we must have at least one
         * parent/guardian details too
         */
        checkGuardianNeeded {
            action {
                // TODO: replace with Config.groovy item
                if (flow.playerCommand.age() < 16 && !playerCommand.parentId)
                    return yes()
                else
                    return no()
            }
            on("yes").to "enterGuardianDetails"
            on("no").to "assignTeam"
        }
        
        /*
         * add parent/guardian details and assign to player
         */
        enterGuardianDetails {
            on ("continue") { PersonCommand personCommand ->
                
            }.to "assignTeam"
        }
        
        /*
         * select a team and enter league reg number if available
         */
        assignTeam {
            on ("continue") {
                
            }.to "payment"
        }
        
        payment()
    } 
}

abstract class AbstractPersonCommand implements Serializable {
    String givenName
    String familyName
    String knownAsName

    static constraints = {
        givenName(nullable:false, blank:false, size:1..50)
        familyName(nullable:false, blank:false, size:1..50)
        knownAsName(nullable:true, size:1..50)
    }
}

class PlayerCommand extends AbstractPersonCommand {
    Date dob

    static constraints = {
        dob(nullable: false)
    }

    /**
     * @return age at cutoff
     */
    int age() {
        // TODO: make cutoff configurable
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

    static constraints = {
        email(email:true, blank: false)
        phone1(blank: false)
        phone2(nullable: true)
    }
}
