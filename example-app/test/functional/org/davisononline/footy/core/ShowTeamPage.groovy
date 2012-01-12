package org.davisononline.footy.core

import org.davisononline.footy.*

class ShowTeamPage extends FootyPage {

    static at = { 
        $('div#iconbar') != null
    }

    static content = {

        // main page links and iconbar
        changeSponsor(required: false) { $('a', text: 'Add/Change Sponsor') }
        sendEmailButton(required: false) { $('a', title: 'Send Email Message') }
        addNewsButton(required: false) { $('a', title: 'Create News Item') }

        // news
        newsForm(required: false) { $('form#news') }
        newsSubmitButton(required: false) { $('input', value: 'Create') }

        // fixtures
        addFixture(required: false) { $('a', text:'New') }
        fixtureAddForm(required: false) { $('form#addFixture') }
        fixtureAddSubmitButton(required: false) { $('input', value: 'Create') }                
    }

    void addNews(subject, body, global = false) {
        addNewsButton.click()
        Thread.sleep(2000) // message box animation delay
        newsForm.subject = subject
        newsForm.body = body
        if (global) newsForm.clubWide = true
        newsSubmitButton.click()
    }
}

