class FootyMatchUrlMappings {

	static mappings = {

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

	}
}
