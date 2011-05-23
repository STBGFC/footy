package org.davisononline.footy.core.util

import org.davisononline.footy.core.*

/**
 * takes a team, or a person record and converts to one or more
 * address records in vcard 3.0 format.
 *
 * @author darren
 * @since 23/05/11
 */
class VCardConverter {

    static void convert(Team team, boolean includeParents, out) {
        convert(team?.manager, out)
        team?.coaches?.each { c ->
            convert(c, out)
        }
        if (includeParents) {
            team?.players?.each { p ->
                convert(p.guardian, out)
            }
        }
    }

    /**
     *
        BEGIN:VCARD
        VERSION:3.0
        N:Gump;Forrest
        FN:Forrest Gump
        ORG:Bubba Gump Shrimp Co.
        TITLE:Shrimp Man
        PHOTO;VALUE=URL;TYPE=GIF:http://www.example.com/dir_photos/my_photo.gif
        TEL;TYPE=WORK,VOICE:(111) 555-1212
        TEL;TYPE=HOME,VOICE:(404) 555-1212
        ADR;TYPE=WORK:;;100 Waters Edge;Baytown;LA;30314;United States of America
        LABEL;TYPE=WORK:100 Waters Edge\nBaytown, LA 30314\nUnited States of America
        ADR;TYPE=HOME:;;42 Plantation St.;Baytown;LA;30314;United States of America
        LABEL;TYPE=HOME:42 Plantation St.\nBaytown, LA 30314\nUnited States of America
        EMAIL;TYPE=PREF,INTERNET:forrestgump@example.com
        REV:20080424T195243Z
        END:VCARD
     *
     * According to the current (3.0) specification, vCards must contain the VERSION, N, and FN properties between
     * the BEGIN:VCARD and END:VCARD entities.
     *
     * @param person
     * @param out
     */
    static void convert(person, out) {
        if (person && out) {
            out.println('BEGIN: VCARD\nVERSION:3.0')
            out.println("N:${person.familyName};${person.givenName}")
            out.println("FN:${person.fullName}")
            out.println("TEL;TYPE=MOBILE,VOICE:${person.phone1}")
            out.println("TEL;TYPE=HOME,VOICE:${person.phone2}")
            out.println("EMAIL;TYPE=PREF,INTERNET:${person.email}")
            out.println("END:VCARD")
        }
    }
}
