package org.davisononline.footy.core

/**
 * represents a tier of registration costs at certain
 * age breaks
 */
class RegistrationTier implements Serializable {
    
    String name
    double amount = 10.00
    double siblingDiscount = 0.00
    double repeatDiscount = 0.00

    static constraints = {
        name (blank: false, unique: true)
    }

    public String toString() {
        name
    }
}
