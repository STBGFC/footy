class TournamentUrlMappings {

	static mappings = {
		"/tournamentsignup/$name" (controller: "entry", action: "apply")

        "/entry/deleteTeam/$tournamentId/$teamId" (controller: "entry", action: "deleteTeam")
	}
}
