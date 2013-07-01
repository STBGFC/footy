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
        mavenRepo "http://m2.modularity.net.au/releases"
    }
    plugins {
        compile ':searchable:0.6.4'
        compile ':export:1.5'
        compile ':mail:1.0.1'
        compile ':webxml:1.4.1'
        compile ':spring-security-core:1.2.6'
        compile ':modalbox:0.4'
        compile ':feeds:1.5'
        compile ':resources:1.1.6'
        compile ':quartz:1.0-RC2'
        runtime ':hibernate:1.3.9'
        runtime ':tomcat:1.3.9'
        runtime ':webflow:1.3.7'
        runtime ':weceem:1.1.2'
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        runtime 'org.imgscalr:imgscalr-lib:4.2'
        runtime 'com.lowagie:itext:2.1.5'
        runtime 'com.lowagie:itext-rtf:2.1.5'
        runtime 'net.fortuna.ical4j:ical4j:1.0.1'
    }
}

