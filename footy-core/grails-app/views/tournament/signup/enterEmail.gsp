<%@ page import="org.davisononline.footy.tournament.Entry" %>
<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title><g:message code="org.davisononline.footy.tournament.views.entry.apply.title" default="${tournament.name} Tournament Entry" /></title>
    </head>
    <body>

        <div class="main" id="main-two-columns">
            <div class="left" id="main-left">


                <h1><g:message code="org.davisononline.footy.tournament.views.entry.apply.title" default="${tournament.name} Tournament Entry" /></h1>
                <div class="dialog">
                    <g:form action="signup" >
                        <p>
                            You are creating team entries for our <strong>${tournament.name}</strong> tournament
                            which is ${tournament.startDate == tournament.endDate ? "on " + formatDate(date:tournament.startDate, format:"dd MMMM yyyy") : "from <strong>" + formatDate(date:tournament.startDate, format:"dd-") + formatDate(date:tournament.endDate, format: "dd MMMM yyyy") + "</strong>"}.
                            If you wish to submit one or more teams to any of the open competitions, please begin by telling
                            us your email address...
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

            </div>

            <div class="right sidebar" id="sidebar-2">
                <h3>Current tournament entry status</h3>
                <table>
                    <tbody>
                        <tr>
                            <th></th>
                            <th class="text-right">Max Places</th>
                            <th class="text-right">Places Taken</th>
                            <th class="text-right">Wait List</th>
                            <th>Entry Status</th>
                        </tr>
                    <g:each in="${tournament.competitions.sort{it.name}}" var="comp">
                        <tr class="${comp.open ? 'openTournament':'closedTournament'}">
                            <td><strong>${comp}</strong></td>
                            <td class="text-right">${comp.teamLimit}</td>
                            <td class="text-right">${comp.entered.size()}</td>
                            <td class="text-right">${comp.waiting.size()}</td>
                            <td>${comp.open ? 'OPEN':'closed'}</td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="clearer">&nbsp;</div>

        </div>
    </body>
</html>
