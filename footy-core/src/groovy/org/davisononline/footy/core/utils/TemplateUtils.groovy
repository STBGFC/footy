package org.davisononline.footy.core.utils

import org.codehaus.groovy.grails.commons.ConfigurationHolder

/**
 * attempting to read a GString in from external sources has issues
 * so this class acts as a helper to manage (for example) email
 * templates in service classes
 */
class TemplateUtils {

    static def eval(String text, binding) {
        def engine = new groovy.text.SimpleTemplateEngine()
        binding.cfg = ConfigurationHolder.config
        def template = engine.createTemplate(text).make(binding)
        template.toString()
    }
    
}
