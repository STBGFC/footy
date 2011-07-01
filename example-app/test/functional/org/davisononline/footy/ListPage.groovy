package org.davisononline.footy

import geb.Page
import geb.Module


/**
 * generic list functionality in a page
 */
class ListModule extends Module {
}

/**
 * generic page with a (possibly paginated) list on it
 */
class ListPage extends Page {
    static content = {
        list { module ListModule }
    }
}

