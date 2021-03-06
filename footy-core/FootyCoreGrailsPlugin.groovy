import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class FootyCoreGrailsPlugin {
    // the plugin version
    def version = "1.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3 > *"
    // the other plugins this plugin depends on
    def dependsOn = [
        mail: "1.0.1 > *", 
        webflow: "1.3.7 > *", 
        export: "1.5 > *",
        weceem:'1.1.2>*',
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

        /*
         * bridge s2-core to weceem.. this code does the same job as the
         * weceem-spring-security plugin which is not required and should not
         * be present.
         */
        def authenticateService = applicationContext.springSecurityService
        applicationContext.wcmSecurityService.securityDelegate = [
            getUserName : { ->
                def princ = authenticateService.principal
                if (log.debugEnabled) {
                    log.debug "Weceem security getUserName callback - user principal is: ${princ} (an instance of ${princ?.class})"
                }
                if (princ instanceof String) {
                    return null
                } else {
                    return princ?.username
                }
            },
            getUserEmail : { ->
                // we manage an email address as part of a Person record and
                // not necessarily at the principal.
                return "unknown@user.tld"
            },
            getUserRoles : { ->
                def princ = authenticateService.principal
                if (log.debugEnabled) {
                    log.debug "Weceem security getUserRoles callback - user principal is: ${princ} (an instance of ${princ?.class})"
                }
                if (princ instanceof String) {
                    return ['ROLE_GUEST']
                }
                def auths = []
                def authorities = princ?.authorities
                if (authorities) {
                    auths.addAll(authorities?.authority)
                }
                return auths ?: ['ROLE_GUEST']
            },
            getUserPrincipal : { ->
                // weceem 1.1.2 FORCES the presence of "firstName", "lastName" and
                // "email" fields on the principal (see RenderEngine.makeUserInfo() )
                //authenticateService.principal
                [firstName:'John', lastName:'Doe', email:'unknown@user.tld']
            }
        ]

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
