
<%@ page import="org.davisononline.footy.core.Team; org.davisononline.footy.core.Sponsor" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'sponsor.label', default: '''Sponsor''')}" />
        <title>
            <g:if test="${sponsorInstance?.id}">${sponsorInstance.name}</g:if>
            <g:else><g:message code="default.create.label" args="[entityName]" /></g:else>
        </title>
    </head>
    <body>
        <div class="dialog">
            <p>
                Create or edit the sponsor details below
            </p>
            <div class="nav">
                <g:render template="/shared/editNavButtons" model="${[entityName:entityName]}"/>
            </div>

            <g:form method="post"  enctype="multipart/form-data">
                <g:hiddenField name="id" value="${sponsorInstance?.id}" />
                <g:hiddenField name="version" value="${sponsorInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="sponsor.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: sponsorInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" maxlength="50" value="${sponsorInstance?.name}" />
                                    <g:render template="/shared/fieldError" model="['instance':sponsorInstance,'field':'name']" plugin="footy-core"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="url"><g:message code="sponsor.url.label" default="URL (Web Address)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: sponsorInstance, field: 'url', 'errors')}">
                                    <g:textField name="url" maxlength="50" value="${sponsorInstance?.url}" />
                                    <g:render template="/shared/fieldError" model="['instance':sponsorInstance,'field':'url']" plugin="footy-core"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="logo"><g:message code="sponsor.logo.label" default="Logo (max size: ${Sponsor.MAX_LOGO_SIZE} bytes)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: sponsorInstance, field: 'logo', 'errors')}">
                                    <input type="file" name="logo"/>
                                    <g:render template="/shared/fieldError" model="['instance':sponsorInstance,'field':'logo']" plugin="footy-core"/>
                                    <g:if test="${sponsorInstance.logo}">
                                    <br/>
                                    <footy:sponsorLogo sponsor="${sponsorInstance}"/>
                                    </g:if>
                                </td>
                            </tr>

                            <g:if test="${sponsorInstance.id}">
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <g:message code="org.davisononline.footy.core.sponsor.teamlist" default="Teams sponsored by ${sponsorInstance.name}"/>
                                </td>
                                <td>
                                    <ul>
                                    <g:each in="${Team.findBySponsor(sponsorInstance)}" var="t">
                                        <li><g:link controller="team" action="edit" id="${t.id}">${t}</g:link> </li>
                                    </g:each>
                                    </ul>
                                </td>
                            </tr>
                            </g:if>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <g:if test="${sponsorInstance?.id}">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                    </g:if>
                    <g:else>
                    <span class="button"><g:actionSubmit class="save" action="save" value="${message(code: 'default.button.save.label', default: 'Save')}" /></span>
                    </g:else>
                </div>
            </g:form>
        </div>
    </body>
</html>
