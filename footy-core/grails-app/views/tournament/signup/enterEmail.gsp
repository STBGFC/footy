<%@ page import="org.davisononline.footy.tournament.Entry" %>
<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title><g:message code="org.davisononline.footy.tournament.views.entry.apply.title" default="${tournament.name} Entry" /></title>
    </head>
    <body>
        <h1><g:message code="org.davisononline.footy.tournament.views.entry.apply.title" default="${tournament.name} Entry" /></h1>
        <div class="dialog">
            <g:form action="signup" >
                <p>
                    You are creating team entries for our <strong>${tournament.name}</strong> tournament
                    which is ${tournament.startDate == tournament.endDate ? "on " + formatDate(date:tournament.startDate, format:"dd MMMM yyyy") : "from " + formatDate(date:tournament.startDate, format:"dd-") + formatDate(date:tournament.endDate, format: "dd MMMM yyyy")}
                </p>
                <h3>Current tournament entry status</h3>
                <table>
                    <tbody>
                        <tr><th>Competition</th><th>Max. Places</th><th>Places Taken</th><th>Waiting List</th></tr>
                    <g:each in="${tournament.competitions.sort{it.name}}" var="comp">
                        <tr class="${comp.open ? 'openTournament':'closedTournament'}">
                            <td>${comp}</td><td>${comp.teamLimit}</td><td>${comp.entered.size()}</td><td>${comp.waiting.size()}</td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>

                <p>
                    Competitions above in green are still open for entry.  If you wish to submit one or more teams
                    to any of the open competitions, please begin by telling us your email address...
                </p>

                <table>
                    <tbody>

                        <tr class="prop">
                            <td  class="name">
                                <label for="email"><g:message code="org.davisononline.footy.tournament.views.entry.apply.email.label" default="Your Email Address" /></label>
                            </td>
                            <td  class="value ${hasErrors(bean: personInstance, field: 'email', 'errors')}">
                                <g:textField name="email" value="${personInstance?.email?.trim()}" />
                                <g:render template="/shared/fieldError" model="['instance':personInstance,'field':'email']" plugin="footy-core"/>
                            </td>
                        </tr>

                    </tbody>
                </table>
                <div class="buttons">
                    <span class="button"><g:submitButton name="submit" class="save" value="${message(code: 'button.contactDetails.submit.label', default: 'Continue...')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
