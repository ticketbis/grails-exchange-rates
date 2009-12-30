

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title><g:message code="exchangeRate.edit" default="Edit Exchange Rate" /></title>
        <g:exchangeRateHelpBalloons />
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="$createLink(uri: '/', absolute: true)}"><g:message code="home" default="Home" /></a></span>
            <g:exchangeRateMenuButton/>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="exchangeRate.list" default="Exchange Rate List" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="exchangeRate.new" default="New Exchange Rate" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="exchangeRate.edit" default="Edit Exchange Rate" /></h1>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:hasErrors bean="${exchangeRateInstance}">
            <div class="errors">
                <g:renderErrors bean="${exchangeRateInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${exchangeRateInstance?.id}" />
                <g:render template="dialog" model="[exchangeRateInstance: exchangeRateInstance]" contextPath="${pluginContextPath}" />
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="Update" value="${message(code:'update', 'default':'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('${message(code:'delete.confirm', 'default':'Are you sure?')}');" action="Delete" value="${message(code:'delete', 'default':'Delete')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
