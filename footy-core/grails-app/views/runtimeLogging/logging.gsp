<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <g:set var="entityName" value="${message(code: 'league.label', default: 'League')}" />
    <title><g:message code="org.grails.runtimelogging.logging.title" default="Runtime Logging Config"/></title>
</head>
<body>
<h1>
    <g:message code="org.grails.runtimelogging.logging.title" default="Runtime Logging Config"/>
</h1>
<div class="dialog">
    <div class="nav">
        <g:render template="/shared/editNavButtons" model="${[entityName:entityName]}"/>
    </div>

    <div class="dialog">
        <table><tbody>

        <tr class="prop">
            <td valign="top" class="name">
                <label><g:message code="org.grails.runtimelogging.domain.label" default="Domain" /></label>
            </td>
            <td valign="top" class="value">
                <g:form action="setLogLevel">
                    <g:select name="logger" from="${domainLoggers}" optionValue="name" optionKey="logger"></g:select>
                    <g:select name="level" from="${logLevels}" value="DEBUG"></g:select>
                    <input type="submit" value="Submit"/>
                </g:form>
            </td>
        </tr>
        
        <tr class="prop">
            <td valign="top" class="name">
                <label><g:message code="org.grails.runtimelogging.controllers.label" default="Controllers" /></label>
            </td>
            <td valign="top" class="value">
                <g:form action="setLogLevel">
                    <g:select name="logger" from="${controllerLoggers}" optionValue="name" optionKey="logger"></g:select>
                    <g:select name="level" from="${logLevels}" value="DEBUG"></g:select>
                    <input type="submit" value="Submit"/>
                </g:form>
            </td>
        </tr>

        <tr class="prop">
            <td valign="top" class="name">
                <label><g:message code="org.grails.runtimelogging.services.label" default="Services" /></label>
            </td>
            <td valign="top" class="value">
                <g:form action="setLogLevel">
                    <g:select name="logger" from="${serviceLoggers}" optionValue="name" optionKey="logger"></g:select>
                    <g:select name="level" from="${logLevels}" value="DEBUG"></g:select>
                    <input type="submit" value="Submit"/>
                </g:form>
            </td>
        </tr>

        <tr class="prop">
            <td valign="top" class="name">
                <label><g:message code="org.grails.runtimelogging.grails.label" default="Grails" /></label>
            </td>
            <td valign="top" class="value">
                <g:form action="setLogLevel">
                    <g:select name="logger" from="${grailsLoggers}" optionValue="name" optionKey="logger"></g:select>
                    <g:select name="level" from="${logLevels}" value="DEBUG"></g:select>
                    <input type="submit" value="Submit"/>
                </g:form>
            </td>
        </tr>

        <tr class="prop">
            <td valign="top" class="name">
                <label><g:message code="org.grails.runtimelogging.other.label" default="Other" /></label>
            </td>
            <td valign="top" class="value">
                <g:form action="setLogLevel">
                    <g:select name="logger" from="${otherLoggers}" optionValue="name" optionKey="logger"></g:select>
                    <g:select name="level" from="${logLevels}" value="DEBUG"></g:select>
                    <input type="submit" value="Submit"/>
                </g:form>
            </td>
        </tr>
        
        
        </tbody></table>
    </div>
</div>

</body>
</html>
