//
// This script is executed by Grails after plugin was installed to project.
// This script is a Gant script so you can use all special variables provided
// by Gant (such as 'baseDir' which points on project base dir). You can
// use 'ant' to access a global instance of AntBuilder
//
// For example you can create directory under project tree:
//
//    ant.mkdir(dir:"${basedir}/grails-app/jobs")
//

def configFile = new File(appDir, 'conf/Config.groovy')
if (configFile.exists()) {
    configFile.withWriterAppend {
        it.writeLine '''

// weceem settings added by footy-cms plugin.  Override per environment
grails.plugins.springsecurity.controllerAnnotations.staticRules = [
    "/wcmEditor/**":         ["hasAnyRole('ROLE_SYSADMIN','ROLE_EDITOR')"],
    "/wcmPortal/**":         ["hasAnyRole('ROLE_SYSADMIN','ROLE_EDITOR')"],
    "/wcmRepository/**":     ["hasAnyRole('ROLE_SYSADMIN','ROLE_EDITOR')"],
    "/wcmSpace/**":          ["hasAnyRole('ROLE_SYSADMIN','ROLE_EDITOR')"],
    "/wcmSychronization/**": ["hasAnyRole('ROLE_SYSADMIN','ROLE_EDITOR')"],
    "/wcmVersion/**":        ["hasAnyRole('ROLE_SYSADMIN','ROLE_EDITOR')"]
]

// weceem settings
//weceem.security.policy.path='/path/to/weceem.policy'
weceem.content.prefix = 'content'
weceem.tools.prefix = 'wcm-tools'
weceem.admin.prefix = 'wcm-admin'
weceem.upload.dir = 'file:///tmp/weceem'
weceem.logout.url = [controller:'logout']
weceem.admin.layout='wcmadmin'
weceem.default.space.template='classpath:/org/weceem/resources/footy-space-template.zip'
weceem.space.templates = [
    'footy' : 'classpath:/org/weceem/resources/footy-space-template.zip'
]
'''
    }
}

