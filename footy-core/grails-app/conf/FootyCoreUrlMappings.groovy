class FootyCoreUrlMappings {

	static mappings = {
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
	}
}
