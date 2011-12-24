package org.davisononline.footy.match

import org.davisononline.footy.core.Team
import org.davisononline.footy.core.utils.DateTimeUtils

class FootyMatchService {

    static transactional = true

    /**
     * returns a list of all fixtures for the supplied team for the season
     *
     * @param team
     * @param n number of fixtures to return, defaults to 10
     * @return
     */
    def getAllFixtures(Team team) {
        def fc = Fixture.createCriteria()
        def list = fc.list () {
            eq ("team", team)
            order ("dateTime", "asc")
        }
        list
    }

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
     * or any that are in the past but have not been played
     *
     * @param myteam
     * @param n
     * @param date
     * @return
     */
    def getFixtures(Team team, int n, Date date) {
        def fc = Fixture.createCriteria()
        def list = fc.list (max:n) {
            eq ("team", team)
            or {
                ge ("dateTime", date)
                eq ("played", false)
            }
            order ("dateTime", "asc")
        }
        list
    }

    /**
     * returns a list of fixtures that are being played at home on a certain date
     *
     * @param date the date that the fixtures are being played
     * @return
     */
    def getHomeGamesOn(Date date) {
        Date from = DateTimeUtils.setMidnight(date)

        def fc = Fixture.createCriteria()
        def list = fc.list () {
            eq ("homeGame", true)
            eq ("played", false)
            and {
                ge ("dateTime", from)
                lt ("dateTime", from + 1)
            }
            order ("dateTime", "asc")
        }
        list
    }
}
