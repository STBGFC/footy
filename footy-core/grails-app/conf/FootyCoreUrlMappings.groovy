class FootyCoreUrlMappings {

	static mappings = {
		// hide standard searchable controller and views from plugin
        "/searchable" (controller:'search')

        // enable 2 ids to be passed to delQualification
        "/person/delQualification/$personId/$qualificationId" (controller:'person', action:'delQualification')

        // nicer URL for viewing teams
        "/u$ageBand/$teamName" (controller:'team', action: 'show')
        "/U$ageBand/$teamName" (controller:'team', action: 'show')

        // reset token link in email
        "/login/reset/$token" (controller:'login', action: 'reset')

		"/"(view:"/index")
		"500"(view:'/error')
	}
}
