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
            blank: false,
            matches: '^([A-PR-UWYZ]([0-9]{1,2}|([A-HK-Y][0-9]|[A-HK-Y][0-9]([0-9]|[ABEHMNPRV-Y]))|[0-9][A-HJKS-UW])\\ [0-9][ABD-HJLNP-UW-Z]{2}|(GIR\\ 0AA)|(SAN\\ TA1)|(BFPO\\ (C\\/O\\ )?[0-9]{1,4})|((ASCN|BBND|[BFS]IQQ|PCRN|STHL|TDCU|TKCA)\\ 1ZZ))$'
        )
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

