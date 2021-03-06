package org.davisononline.footy.core

/**
 * can be applied to a Person to denote a Coach, Referee or simply
 * to show qualifications available
 */
class Qualification implements Comparable, Serializable {

    QualificationType type
    Date attainedOn
    Date expiresOn

    static belongsTo = [person: Person]

    static constraints = {
        expiresOn nullable: true
    }
    
    /**
     * sort date-wise
     */
    int compareTo(obj) {
        try {
            def d1 = attainedOn.compareTo(obj?.attainedOn)
            if (d1 == 0)
                type?.name?.compareTo(obj?.type?.name)
            else
                d1
        }
        catch (Exception ex) {
            log.error ex
            return 1
        }
    }

    QualificationType getType() {
        type
    }

    void setType(QualificationType value) {
        this.type = value
        calculateExpiresOn()
    }

    Date getAttainedOn() {
        attainedOn
    }
    
    void setAttainedOn(Date value) {
        this.attainedOn = value
        calculateExpiresOn()
    }

    private calculateExpiresOn() {
        if (!expiresOn && type?.yearsValidFor > 0 && attainedOn != null) {
            def c = Calendar.instance
            c.time = attainedOn
            c.add(Calendar.YEAR, type.yearsValidFor)
            expiresOn = c.time -1
        }
    }

    public String toString() {
        if (expiresOn)
            "${type} (expires: ${expiresOn.format('yyyy/MM/dd')})"
        else
            "${type} (attained: ${attainedOn.format('yyyy/MM/dd')})"
    }
}
