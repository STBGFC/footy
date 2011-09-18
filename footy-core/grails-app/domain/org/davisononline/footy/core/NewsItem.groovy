package org.davisononline.footy.core

/**
 * simple record of a subject/body news item that can be displayed on the team
 * site or emailed out etc.
 */
class NewsItem {

    Date createdDate = new Date()
    String subject
    String body
    boolean clubWide = false

    static belongsTo = [team:Team]

    static constraints = {
        subject size: 3..50
    }

    static mapping = {
        body type: 'text'
    }

    String toString() {
        "${team} (${createdDate}): ${subject}"
    }

    String abstractText() {
        if (body.length() < 300) body
        else "${body[0..300]} ..."
    }
}
