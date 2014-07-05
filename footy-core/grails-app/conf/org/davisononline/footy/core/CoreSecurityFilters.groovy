package org.davisononline.footy.core

/**
 * attempt to prevent coaches performing ROLE_COACH activities for teams that
 * they are not managers of
 */
class CoreSecurityFilters {

    def footySecurityService

    def filters = {
        playerPersonEdit(controller: '(player|person)', action: '(edit|update)') {
            before = {
                def allowed
                if (controllerName == 'player')
                    allowed = footySecurityService.canEditPlayer(params.id)
                else
                    allowed = footySecurityService.canEditPerson(params.id)

                if (!allowed) {
                    response.sendError 403
                    return false
                }
            }
        }

        newsAdd(controller: 'team', action: 'addNews') {
            before = {
                if (!footySecurityService.isAuthorisedForManager(params['team.id'])) {
                    response.sendError 403
                    return false
                }
            }
        }
    }

}
