class FootyCoreUrlMappings {

	static mappings = {
		// hide standard searchable controller and views
        "/searchable" (controller:'search')
		"/"(view:"/index")
		"500"(view:'/error')
	}
}
