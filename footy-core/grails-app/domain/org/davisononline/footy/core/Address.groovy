package org.davisononline.footy.core

/**
 * Address
 * 
 * @author Darren Davison
 */
class Address implements Serializable {

    static final VALID_POSTCODE_REGEX = '^([A-PR-UWYZ]([0-9]{1,2}|([A-HK-Y][0-9]|[A-HK-Y][0-9]([0-9]|[ABEHMNPRV-Y]))|[0-9][A-HJKS-UW])\\ [0-9][ABD-HJLNP-UW-Z]{2}|(GIR\\ 0AA)|(SAN\\ TA1)|(BFPO\\ (C\\/O\\ )?[0-9]{1,4})|((ASCN|BBND|[BFS]IQQ|PCRN|STHL|TDCU|TKCA)\\ 1ZZ))$'

    // allow for suggested queries, but do not allow Address objects to show up as independant results
    static searchable = {
        spellCheck "include"
        root false
    }
    
    String name = "Home"
    String house
    String address
    String town
    String postCode

    static constraints = {
        house(blank: false)
        address(blank: false)
        town(nullable: true)
        postCode(
            blank: false,
            matches: VALID_POSTCODE_REGEX
        )
    }

    /**
     * upper case all post codes
     */
    void setPostCode(String postCode) {
        this.postCode = postCode?.toUpperCase()
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
        "$house $address" + (town ? ", ${town}" : "") + ". ${postCode}"
    }

}

