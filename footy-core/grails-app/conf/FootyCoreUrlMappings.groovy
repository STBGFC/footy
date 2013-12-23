import grails.util.GrailsUtil

class FootyCoreUrlMappings {

	static mappings = {
        /*
         * defaults
         */
        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')

        /*
         * main
         */
		// hide standard searchable controller and views from plugin
        "/searchable" (controller:'search')

        // enable 2 ids to be passed to delQualification
        "/person/delQualification/$personId/$qualificationId" (controller:'person', action:'delQualification')

        // nicer URL for viewing teams
        "/u$ageBand/$teamName" {
            controller = 'team'
            action = 'show'
            constraints {
                ageBand(matches: /\d{1,2}/)
            }
        }
        "/U$ageBand/$teamName" {
            controller = 'team'
            action = 'show'
            constraints {
                ageBand(matches: /\d{1,2}/)
            }
        }

        // reset token link in email
        "/login/reset/$token" (controller:'login', action: 'reset')

        // site wide news feed
        "/feed" (controller: 'home', action: 'feed')


        /*
         * tournament
         */
        "/entry/delete/$tournamentId/$compId/$entryId" (controller: "tournament", action: "deleteEntry")
        "/entry/promote/$tournamentId/$compId/$entryId" (controller: "tournament", action: "promoteEntry")
        "/entry/relegate/$tournamentId/$compId/$entryId" (controller: "tournament", action: "relegateEntry")
        "/tournament/signup/$name" (controller: "tournament", action: "signup")


        /*
         * match
         */
        // controller choice of date
        "/pitches" {
            controller = 'resource'
            action = 'summary'
        }

        // date params for fixture allocation summary
        "/pitches/$year/$month/$day" {
            controller = 'resource'
            action = 'summary'
            constraints {
                year(matches: /\d{4}/)
                month(matches: /\d{1,2}/)
                day(matches: /\d{1,2}/)
            }
        }

        // date params for ref reports
        "/resource/reports/$year/$month/$day" {
            controller = 'resource'
            action = 'reports'
            constraints {
                year(matches: /\d{4}/)
                month(matches: /\d{1,2}/)
                day(matches: /\d{1,2}/)
            }
        }


        /*
         * paypal
         */
        "/paypal/buy/$transactionId?"(controller:"paypal", action:"buy")
        "/paypal/cart/$transactionId"(controller:"paypal", action:"uploadCart")
        "/paypal/notify/$buyerId/$transactionId"(controller:"paypal", action:"notify")
        "/paypal/success/$buyerId/$transactionId"(controller:"paypal", action:"success")
        "/paypal/cancel/$buyerId/$transactionId"(controller:"paypal", action:"cancel")

        if (GrailsUtil.isDevelopmentEnv()) {
            "/paypal/test"(view:"/paypal/test")
        }
	}
}
