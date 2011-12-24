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

    static constraints = {
        name blank: false, nullable: false
        type inList: [PITCH, CHANGING_ROOM]
    }

    public String toString() {
        "${type} ${name}"
    }
}
