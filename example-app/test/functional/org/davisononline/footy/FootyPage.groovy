package org.davisononline.footy

import geb.Page

class FootyPage extends Page {

    static content = {
        auth { module AuthModule }
        msg { $("div.message").text() }
    }

}

