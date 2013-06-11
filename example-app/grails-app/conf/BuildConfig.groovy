grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.plugin.location.'footy-core' = "../footy-core"

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
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.13'
        test("org.springframework:spring-test:3.0.5.RELEASE")
        test("org.codehaus.geb:geb-junit4:0.6.2")
        test("org.seleniumhq.selenium:selenium-support:2.15.0")
        //test("org.seleniumhq.selenium:selenium-chrome-driver:2.15.0")
        test("org.seleniumhq.selenium:selenium-firefox-driver:2.15.0")
        //test("org.seleniumhq.selenium:selenium-ie-driver:2.0rc3")
        test("dumbster:dumbster:1.6")
    }
    plugins {
        test ":geb:0.6.0"
    }
}
