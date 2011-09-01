package org.davisononline.footy.match

import org.davisononline.footy.core.Team

class FootyMatchService {

    static transactional = true

    /**
     * returns a list of up to n fixtures for the supplied team from the current date
     * (ie future)
     *
     * @param team
     * @param n number of fixtures to return, defaults to 10
     * @return
     */
    def getFixtures(Team team, int n = 10) {
        getFixtures(team, n, new Date())
    }

    /**
     * returns a list of up to n fixtures for the supplied team from the supplied date
     *
     * @param myteam
     * @param n
     * @param date
     * @return
     */
    def getFixtures(Team team, int n, Date date) {
        def fc = Fixture.createCriteria()
        def list = fc.list (max:n) {
            or {
                eq ("homeTeam", team)
                eq ("awayTeam", team)
            }
            ge ("dateTime", date)
            order ("dateTime", "asc")
        }
        list
    }
}
