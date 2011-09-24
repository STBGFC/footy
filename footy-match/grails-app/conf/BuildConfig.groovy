grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.plugin.location.'footy-core' = "../footy-core"

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
        //mavenLocal()
        //mavenCentral()

        // iCal4j.  Not using the grails ic-alendar plugin as it causes stack overflows in the render() methods
        mavenRepo "http://m2.modularity.net.au/releases"
    }
    plugins {
        //compile ':ic-alendar:0.3.2' // <-- 0.3.2 is borked
    }
    dependencies {
        runtime 'net.fortuna.ical4j:ical4j:1.0.1'
    }
}
