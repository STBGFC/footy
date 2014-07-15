package org.davisononline.footy.core

import grails.plugins.springsecurity.Secured

class ClubController {

    def index = { }

    @Secured(["ROLE_CLUB_ADMIN"])
    def addresscards = {
        response.setHeader("Content-disposition", "attachment;filename=${Club.homeClub.name.replace(" ", "_")}_all_contacts.vcf")
        def contacts = Person.findAllByEligibleParentAndAddressIsNotNull(true)
        render (
            template: '/team/vcard',
            plugin: 'footy-core',
            collection: contacts,
            contentType: 'text/x-vcard'
        )
    }
}
