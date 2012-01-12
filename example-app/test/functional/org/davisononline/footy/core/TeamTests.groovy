package org.davisononline.footy.core

import org.davisononline.footy.*

class TeamTests extends AbstractTestHelper {

    def doRascalsLogin() {
        go ''
        login "Manager3", "Manager3"
        $('a', text: 'U11 Rascals').click()
        waitFor { at(ShowTeamPage) }
    }

    def doTeamNews(subject, global) {
        // use the u11 Rascals team and p3 manager created in BootStrap
        doRascalsLogin()
        addNews(subject, 'team news body', global)
        assert msg == "News saved to page."
        assert $('h2', text: 'team news') != null
    }

    void testAddTeamNewsTeamPage() {
        doTeamNews('team news', false)
        auth.logout()
    }

    void testAddTeamNewsHomePage() {
        doTeamNews('home news', true)
        go ''
        assert $('div.newsItem', 0).find('h2').text() == 'home news'
        auth.logout()
    }

    void testAddFixtures() {
        def oppo = 'Tony Testing Tigers'
        doRascalsLogin()
        addFixture.click()
        Thread.sleep(1200) // dialog open
        // default LeagueGame
        fixtureAddForm.dateTime_month.value('January')
        fixtureAddForm.dateTime_day.value('1')
        fixtureAddForm.dateTime_hour.value('9')
        fixtureAddForm.dateTime_minute.value('00')
        fixtureAddForm.opposition2 = oppo
        fixtureAddSubmitButton.click()
        assert $('a', text: oppo) != null
    }

}

