package org.davisononline.footy

import geb.Module

/**
 * module for flow pages - normally have optional validation errors and
 * a continue button at least
 */
class FlowModule extends Module {
    static content =  {
        errors { $("div.errors") }
        error { i -> errors[i] }        
        contButton { $("input", value:"Continue") }
    }
}

