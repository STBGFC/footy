grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        mavenLocal()
        mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    plugins {
        compile ':paypal:0.6.5'
        compile ':searchable:0.6.2'
        compile ':export:0.8'
        compile ':mail:1.0-SNAPSHOT'
        compile ':spring-security-core:1.2.1'
        compile ':modalbox:0.4'
        compile ':cache-headers:1.1.5'
        runtime ':hibernate:1.3.7'
        runtime ':webflow:1.3.7'
        runtime ':tomcat:1.3.7'
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        //compile 'org.grails.plugins:mail:1.0-SNAPSHOT'
        //compile 'paypal:paypal:latest.integration'
        //runtime 'org.grails.plugins:webflow:latest.integration'

    }
}
