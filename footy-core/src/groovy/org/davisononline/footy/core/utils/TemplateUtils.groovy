package org.davisononline.footy.core.utils

/**
 * attempting to read a GString in from external sources has issues
 * so this class acts as a helper to manage (for example) email
 * templates in service classes
 */
class TemplateUtils {

    static def eval(String text, binding) {
        def engine = new groovy.text.SimpleTemplateEngine()
        def template = engine.createTemplate(text).make(binding)
        template.toString()
    }
    
}
