package org.davisononline.footy.core.jobs

import grails.util.Environment
import org.codehaus.groovy.grails.commons.ConfigurationHolder


class UpdateStandingsJob {

    // 2hrs by default
    static long LEAGUE_TABLE_LOOKUP_INTERVAL = ConfigurationHolder.config?.org?.davisononline?.footy?.core?.fulltime?.interval ?: 7200000

    def leagueService

    static triggers = {
        simple name: 'simpleTrigger', startDelay: 60000, repeatInterval: LEAGUE_TABLE_LOOKUP_INTERVAL
    }

    def execute(){
        if (Environment.getCurrent().equals(Environment.PRODUCTION)) {
            log.info "Starting job execution; UpdateStandings"
            leagueService.updateAllLeagueTables()
        }
    }
}
