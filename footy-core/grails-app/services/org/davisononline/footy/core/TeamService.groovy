package org.davisononline.footy.core

import org.davisononline.footy.match.RefereeReport
import org.davisononline.footy.match.Fixture

class TeamService {

    static transactional = true

    def deleteTeam(Team team) {
        // remove ref reports/fixtures
        def fixtures = Fixture.findAllByTeam(team)
        fixtures.each { fixture ->
            def reports = RefereeReport.findAllByFixture(fixture)
            reports.each { it.delete() }
            fixture.delete()
        }

        team.players.each {player ->
            player.team = null
            player.save()
        }

        team.players = null
        team.delete(flush: true)
    }
}
