package org.davisononline.footy.core

import org.davisononline.footy.match.RefereeReport
import org.davisononline.footy.match.Fixture

class TeamService {

    static transactional = true

    def deleteTeam(Team team) {

        log.info "Deleting Team ${team}"

        // remove ref reports/fixtures
        def fixtures = Fixture.findAllByTeam(team)
        log.debug "${fixtures.size()} Fixtures found for ${team}"
        fixtures.each { fixture ->
            def reports = RefereeReport.findAllByFixture(fixture)
            if (reports.size() > 0) {
                log.debug "Removing referee reports for Fixture ${fixture}"
            }
            reports.each { it.delete() }

            // somehow, resources are not auto deleted sometimes #107
            if (fixture.resources.size() > 0) {
                log.debug "Removing resources for Fixture ${fixture}"
            }
            fixture.resources.each { it.delete() }

            log.debug "Deleting Fixture ${fixture}"
            fixture.delete()
        }

        team.players.each {player ->
            log.debug "Unassigning Player ${player} from Team ${team}"
            player.team = null
            player.save()
        }

        team.players = null
        team.delete(flush: true)
        log.debug "Team deleted"
    }
}
