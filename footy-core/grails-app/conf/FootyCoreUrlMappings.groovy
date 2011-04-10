class FootyCoreUrlMappings {

	static mappings = {
		// hide standard searchable controller and views from plugin
        "/searchable" (controller:'search')

        // enable 2 ids to be passed to delQualification
        "/person/delQualification/$personId/$qualificationId" (controller:'person', action:'delQualification')

		"/"(view:"/index")
		"500"(view:'/error')
	}
}
