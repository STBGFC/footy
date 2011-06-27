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

    /**
     * creates a new Registration based on the tier supplied and the current
     * system date.
     *
     * @param tier
     * @return
     */
    static createFrom(tier) {
        def renewOn = new Date()
        def mth = renewOn[Calendar.MONTH] + tier.monthsValidFor
        renewOn.set(month: mth)
        new Registration(tier: tier, date: renewOn)
    }

    int compareTo(other) {
        try {
            date.compareTo(other.date)
        }
        catch (Exception ex) {
            log.error ex
            return 1
        }
    }

    String toString() {
        date.format('dd/MM/yyyy')
    }
}
