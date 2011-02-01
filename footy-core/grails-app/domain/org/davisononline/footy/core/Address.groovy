package org.davisononline.footy.core

/**
 * Address
 * 
 * @author Darren Davison
 */
class Address implements Serializable {
    
    String name = "Home"
    String address
    String town
    String postCode

    static constraints = {
        address(blank: false)
        town(nullable: true)
        postCode(
            matches: '^([A-PR-UWYZ]([0-9]{1,2}|([A-HK-Y][0-9]|[A-HK-Y][0-9]([0-9]|[ABEHMNPRV-Y]))|[0-9][A-HJKS-UW])\\ [0-9][ABD-HJLNP-UW-Z]{2}|(GIR\\ 0AA)|(SAN\\ TA1)|(BFPO\\ (C\\/O\\ )?[0-9]{1,4})|((ASCN|BBND|[BFS]IQQ|PCRN|STHL|TDCU|TKCA)\\ 1ZZ))$'
        )
    }
    
    /**
     * expects a String containing a \n separated
     * set of fields with PostCode as the last one.
     * 
     * @param input
     * @return an Address with best effort parsing, so 
     * is NOT guaranteed to pass a validate() method.
     */
    static Address parse(input) {
        try {
            def parts = input.split('\n')
            def a = new Address(
                postCode: parts[parts.length - 1]
            )
            if (parts.length > 2)
                a.town = parts[parts.length - 2]
        
            a.address = parts[0..parts.length-3].join(', ')

            return a 
        }
        catch (Exception ex) {
            println "Cannot parse [$input] as an Address: $ex"
            return null
        }
    }

    /**
     * upper case all post codes
     */
    void setPostCode(String postCode) {
        this.postCode = postCode.toUpperCase()
    }

    /**
     * @return the postCode
     */
    String getPostCode() {
        return postCode
    }

    /**
     * @see java.lang.Object#toString()
     */
    String toString() {
        "${address}\n${town}.\n${postCode}"
    }

}

