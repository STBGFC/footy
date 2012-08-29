package org.davisononline.footy.core

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class LeagueService {

    static random = new Random()

    static transactional = true

    static String FULL_TIME_URL = ConfigurationHolder.config?.org?.davisononline?.footy?.core?.fulltime?.url ?:
        "http://full-time.thefa.com/js/cs1.do"

    static long LEAGUE_TABLE_LOOKUP_TIMEOUT = ConfigurationHolder.config?.org?.davisononline?.footy?.core?.fulltime?.timeout ?: 10000
    

    /**
     * attempt to offline the lookup and storage of league table data for all
     * valid divisions (all those with a code set)
     *
     * @return
     */
    def updateAllLeagueTables() {
        Division.findAllByCodeIsNotNull().each {updateLeagueTable(it)}
    }

    /**
     * update a division with the latest copy of the league table.
     *
     * @param division
     * @return
     */
    def updateLeagueTable(Division division) {
        if (!division.code || division.code == '') {
            log.info "Cannot update [$division] with no code set"
        }
        else {
            def divUrl = FULL_TIME_URL + "?cs=${division.code}&random=${random.nextLong()}"
            URLConnection conn = new URL(divUrl).openConnection()
            conn.connectTimeout = LEAGUE_TABLE_LOOKUP_TIMEOUT
            conn.readTimeout = LEAGUE_TABLE_LOOKUP_TIMEOUT

            try {
                String result = conn.content.text

                if (conn.responseCode == 200 && result.startsWith('document.getElementById')) {
                    result = result[result.indexOf("innerHTML = ")+13..result.lastIndexOf("'")-1]
                    division.standings = result
                    division.save()
                }
                else {
                    log.warn "Unexpected HTTP response, got code ${conn.responseCode} and response [${result[0..40]}...] attempting to update division [${division}]"
                }

            }
            catch (Exception ex) {
                log.warn "Unable to update table for Division [$division] - $ex"
            }
        }
    }
}
