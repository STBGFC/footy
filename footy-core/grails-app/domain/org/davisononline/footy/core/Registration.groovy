package org.davisononline.footy.core

/**
 * Registration links a Player to a RegistrationTier and holds the date of the
 * registration event itself.  The registration ID also acts as the item
 * number of the PayPal PaymentItem, though this cannot be enforced through
 * association.
 */
class Registration implements Serializable, Comparable {

    Date date
    RegistrationTier tier

    static belongsTo = [player: Player]

    static constraints = {
    }

    int compareTo(other) {
        if (!other) return 1
        return date.compareTo(other)
    }

    String toString() {
        date.format('dd/MM/yyyy')
    }
}
