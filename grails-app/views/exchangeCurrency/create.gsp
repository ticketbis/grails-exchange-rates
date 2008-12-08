

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title><g:message code="exchangeCurrency.create" default="Create Exchange Currency" /></title>
        <g:exchangeRateHelpBalloons />
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}"><g:message code="home" default="Home" /></a></span>
            <g:exchangeRateMenuButton/>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="exchangeCurrency.list" default="Exchange Currency List" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="exchangeCurrency.create" default="Create Exchange Currency" /></h1>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:hasErrors bean="${exchangeCurrencyInstance}">
            <div class="errors">
                <g:renderErrors bean="${exchangeCurrencyInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <g:render template="dialog" model="[exchangeCurrencyInstance: exchangeCurrencyInstance]" contextPath="${pluginContextPath}" />
                <div class="buttons">
                    <span class="button"><input class="save" type="submit" value="${message(code:'create', 'default':'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
