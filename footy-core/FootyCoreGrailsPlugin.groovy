import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class FootyCoreGrailsPlugin {
    // the plugin version
    def version = "1.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3 > *"
    // the other plugins this plugin depends on
    def dependsOn = [
        mail: "1.0-SNAPSHOT > *", 
        paypal: "0.6.3 > *", 
    ]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def author = "Darren Davison"
    def authorEmail = "darren@davisononline.org"
    def title = "Footy manager core domain"
    def description = '''\\
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/footy-core"

    def doWithWebDescriptor = { xml ->
        // Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
        // Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        SpringSecurityUtils.securityConfig.userLookup.userDomainClassName = 'org.davisononline.footy.core.SecUser'
        SpringSecurityUtils.securityConfig.userLookup.authorityJoinClassName = 'org.davisononline.footy.core.SecUserSecRole'
        SpringSecurityUtils.securityConfig.authority.className = 'org.davisononline.footy.core.SecRole'
    }

    def onChange = { event ->
        // Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
