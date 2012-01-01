// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

grails.config.locations = [ "classpath:${appName}-config.properties",
                            "classpath:common-config.properties" ]

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// The default codec used to encode data with ${}
grails.views.default.codec = "html" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// whether to install the java.util.logging bridge for sl4j. Disable for AppEngine!
grails.logging.jul.usebridge = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []


// local
grails.plugins.springsecurity.userLookup.userDomainClassName = 'org.davisononline.footy.core.SecUser'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'org.davisononline.footy.core.SecUserSecRole'
grails.plugins.springsecurity.authority.className = 'org.davisononline.footy.core.SecRole'
grails.plugins.springsecurity.successHandler.defaultTargetUrl = '/login/profile'
grails.plugins.springsecurity.roleHierarchy = '''
   ROLE_SYSADMIN > ROLE_OFFICER
   ROLE_OFFICER > ROLE_CLUB_ADMIN
   ROLE_OFFICER > ROLE_TOURNAMENT_ADMIN
   ROLE_OFFICER > ROLE_FIXTURE_ADMIN
   ROLE_CLUB_ADMIN > ROLE_EDITOR
   ROLE_CLUB_ADMIN > ROLE_COACH
'''

grails.mail.host='mail'
org.davisononline.footy.core.homeclubname='Example FC'
org.davisononline.footy.core.registration.email="registration-confirm@examplefc.com"
org.davisononline.footy.core.registration.emailbody='''(Automatic email, please do not reply to this address)

Dear ${buyer.givenName},

Thank you for registering at ExampleFC.  This email confirms that the
following players are registered in the database:

${registrations*.player.join('\n')}

Your invoice number is ${payment.transactionId} and you can access this
at http://www.examplefc.com/invoice/show/${payment.transactionId}

Kind regards,
ExampleFC Registrations.
'''

org.davisononline.footy.core.resetPassword.emailbody='''
Hi ${person.knownAsName ?: person.givenName},

We received a request to reset the password for your account.  If you
didn't make this request, you can just ignore the email and no changes
will be made to your account.

To continue with the password reset, click the link below or copy and
paste into your browser:

${cfg.grails.serverURL}/login/reset/${link}

Please note, this link is only valid for 24 hours.

Kind regards,
Club Admin.
'''

org.davisononline.footy.core.resetComplete.emailbody='''
Hi ${person.knownAsName ?: person.givenName},

You have been assigned a temporary password of:

${pwd}

When you next login, you will be asked to change it.

Kind regards,
Club Admin.
'''

org.davisononline.footy.tournament.registration.email="tournament-confirm@examplefc.com"
org.davisononline.footy.tournament.registration.emailbody='''(Automatic email, please do not reply to this address)

Hi ${entry.contact.knownAsName ?: entry.contact.givenName},

Thank you for your entry to the ExampleFC tournament.  This email confirms that
the the following teams are entered into the ${entry.tournament.name}
competition:

${entry.teams.join(' \\n')}

Your invoice number is ${entry.payment.transactionId} and you can access this
at http://www.examplefc.com/invoice/show/${entry.payment.transactionId}

We'll see you there!
ExampleFC Tournament Committee.
'''

cache.headers.presets = [
    authed_page: false, // No caching for logged in user
    content: [shared:false, validFor: 3600], // 1hr on content
    pics: [shared: true, validFor: 3600*24], // 1 day on pictures

]

// set per-environment serverURL stem for creating absolute links
environments {
    production {
        // following s/be overridden in external prod config
        grails.serverURL = "http://www.examplefc.com"
        grails.paypal.server = "https://www.paypal.com/cgi-bin/webscr"
        grails.paypal.email = "realemail@examplefc.com"
    }
    development {
        grails.serverURL = "http://localhost:8080/${appName}"
        grails.paypal.server = "https://www.sandbox.paypal.com/cgi-bin/webscr"
        grails.paypal.email = "seller_1295042208_biz@googlemail.com"
        grails.mail.overrideAddress="example@examplefc.com"
    }
    test {
        grails.serverURL = "http://localhost:8080/${appName}"
        grails.paypal.server = "https://www.sandbox.paypal.com/cgi-bin/webscr"
        grails.paypal.email = "seller_1295042208_biz@googlemail.com"
        grails.mail.overrideAddress="example@examplefc.com"
        grails.mail.disabled = true // for func. tests
    }
}

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    warn   'org.mortbay.log'

    debug  'grails.app'
           //'org.springframework.jdbc',
           //'org.springframework.orm',
           //'org.hibernate',
           //'org.codehaus.groovy.grails.orm.hibernate'
}
