<%@ page import="org.davisononline.footy.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:set var="entityName" value="${message(code: 'person.label', default: 'Person')}" />
        <title>${personCommand?.id ? "Edit details for: " + personCommand : "Add New Person"}</title>
    </head>
    <body>
        <div class="dialog">
            <g:if test="${!personCommand?.id}">
            <p>
                Please add details of the new person here.  Do NOT use this form to register a new player,
                that must be done using the <g:link controller="registration">registration wizard</g:link>
                instead.
            </p>
            </g:if>
            <g:else>
            <p>
                Amend any details of the person you need to, and then click "Update"
            </p>
            </g:else>
            
            <div class="nav">
                <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
                <span class="menuButton"><g:link class="list" action="listperson"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            </div>
            
            <g:form method="post" >
                <g:hiddenField name="id" value="${personCommand?.id}" />
                <g:hiddenField name="version" value="${personCommand?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                            <g:render template="/personFormBody" />
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <g:if test="${personCommand?.id}">
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
