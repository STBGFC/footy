package org.davisononline.footy.core

/**
 * can be applied to a Person to denote a Coach, Referee or simply
 * to show qualifications available
 */
class Qualification {

    QualificationType type
    Date attainedOn = new Date()
    Date expiresOn = new Date(0)

    static constraints = {
        expiresOn validator: { val, obj ->
                return (obj.type.expires ? val > obj.attainedOn : true)
        }
    }

    public String toString() {
        if (type.expires)
            "${type} (expires: ${expiresOn.format('yyyy/MM/dd')})"
        else
            "${type} (attained: ${attainedOn.format('yyyy/MM/dd')})"
    }
}
