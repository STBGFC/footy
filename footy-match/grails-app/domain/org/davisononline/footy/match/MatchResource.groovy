package org.davisononline.footy.match

/**
 * something that can be assigned to a fixture - typically things like a pitch,
 * set of changing rooms, certain equipment etc.
 */
class MatchResource {

    public static final String PITCH = "Pitch"
    public static final String CHANGING_ROOM = "Changing Room"

    String type
    String name

    static belongsTo = [Fixture]
    
    static constraints = {
        name blank: false, nullable: false, unique: ['type']
        type inList: [PITCH, CHANGING_ROOM]
    }

    public String toString() {
        "${type} ${name}"
    }

    /**
     * used in comparison collections
     *
     * @param o
     * @return
     */
    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof MatchResource)) return false

        MatchResource that = (MatchResource) o

        if (name != that.name) return false
        if (type != that.type) return false

        return true
    }

    int hashCode() {
        int result
        result = type.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}
