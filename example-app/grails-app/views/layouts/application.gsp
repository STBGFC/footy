<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
        <title><g:layoutTitle default="Example FC" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <g:layoutHead />
        <g:javascript library="application" />
        <modalbox:modalIncludes />
    </head>
    <body>
        <div id="banner">
            <g:message code="site.banner.text" default="Example FC"/>
        </div>
        <div id="loginBanner">
            <sec:ifNotLoggedIn>
            <form id="login" action="${resource(file: 'j_spring_security_check')}" method="post">
                <p>
                    username: <g:textField name="j_username"/>
                    password: <g:passwordField name="j_password"/>
                    <a href="#" onclick="document.getElementById('login').submit()">login</a>
                </p>
            </form>
            </sec:ifNotLoggedIn>
            <sec:ifLoggedIn>
            <div>
                Logged in: <span id="username"><strong><sec:username /></strong></span> 
                <g:link controller="logout">[logout]</g:link>
                <g:link controller="login" action="profile">[profile]</g:link>
            </div>
            </sec:ifLoggedIn>
        </div>

        <sec:ifLoggedIn>
        <div id="searchBanner">
            <g:form url='[controller: "search", action: "index"]' name="searchableForm" method="get">
                <g:textField name="q" value="Search..." onfocus="this.value=''" onblur="this.value='Search...'" size="50"/>
            </g:form>
        </div>
        </sec:ifLoggedIn>

        <div class="body">
            <h1><g:layoutTitle default="Example FC"/></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:layoutBody />
        </div>
        <div id="footer">
            <g:message code="site.footer.text" default="(c) Example FC"/>
        </div>
    </body>
</html>

