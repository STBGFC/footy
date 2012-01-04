package org.davisononline.footy.core

import org.codehaus.groovy.grails.commons.ConfigurationHolder

/**
 * team or club financial sponsor
 */
class Sponsor implements Serializable {

    public static final int MAX_LOGO_SIZE =
        ConfigurationHolder.config?.org?.davisononline?.footy?.core?.sponsor?.logoMaxSizeBytes ?: 50*1024
    
    String name
    byte[] logo
    String url

    static constraints = {
        name blank: false
        logo nullable:true, maxSize: MAX_LOGO_SIZE
        url nullable:true, url: true
    }

    String toString() {
        name
    }
    
}
