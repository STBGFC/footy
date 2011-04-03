package org.davisononline.footy.core

/**
 * can be applied to a Person to denote a Coach, Referee or simply
 * to show qualifications available
 */
class Qualification implements Comparable, Serializable {

    QualificationType type
    Date attainedOn = new Date()
    Date expiresOn

    static belongsTo = Person

    static constraints = {
        expiresOn nullable: true
    }
    
    /**
     * sort date-wise
     */
    int compareTo(obj) {
        return attainedOn.compareTo(obj.attainedOn)        
    }

    void setType(value) {
        this.type = value
        calculateExpiresOn()
    }

    void setAttainedOn(value) {
        this.attainedOn = value
        calculateExpiresOn()
    }

    private calculateExpiresOn() {
        if (type?.yearsValidFor > 0 && attainedOn != null) {
            def c = Calendar.instance
            c.time = attainedOn
            c.add(Calendar.YEAR, type.yearsValidFor)
            expiresOn = c.time
        }
    }

    public String toString() {
        if (expiresOn)
            "${type} (expires: ${expiresOn.format('yyyy/MM/dd')})"
        else
            "${type} (attained: ${attainedOn.format('yyyy/MM/dd')})"
    }
}
