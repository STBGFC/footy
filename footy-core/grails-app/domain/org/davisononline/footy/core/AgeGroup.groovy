package org.davisononline.footy.core

class AgeGroup implements Comparable, Serializable {

    int year
    boolean under = true
    Person coordinator


    static constraints = {
        year inList: [6,7,8,9,10,11,12,13,14,15,16,17,18,21,35], unique: ['under']
        coordinator nullable: true
    }

    @Override
    public String toString() {
        "${under ? 'U' : 'Over '}$year"
    }

    @Override
    int compareTo(Object o) {
        AgeGroup other = (o)
        return other.year - year
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        AgeGroup ageGroup = (AgeGroup) o

        if (under != ageGroup.under) return false
        if (year != ageGroup.year) return false

        return true
    }

    int hashCode() {
        int result
        result = year
        result = 31 * result + (under ? 1 : 0)
        return result
    }
}
