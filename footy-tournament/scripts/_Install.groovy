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

/*
 * this REALLY shouldn't be necessary.  If the grails plugin
 * architecture was slightly better thought out, the host
 * application would be able to utilise the paypal views from 
 * within here.
 */
ant.mkdir(dir: "${basedir}/grails-app/views/paypal")
ant.copy(
    file:  "${pluginBasedir}/grails-app/views/paypal/*",
    todir: "${basedir}/grails-app/conf"
)
