

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title><g:message code="exchangeCurrency.list" default="Exchange Currency List" /></title>
        <g:exchangeRateHelpBalloons />
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/', absolute: true)}"><g:message code="home" default="Home" /></a></span>
            <g:exchangeRateMenuButton/>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="exchangeCurrency.new" default="New Exchange Currency" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="exchangeCurrency.list" default="Exchange Currency List" /></h1>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:exchangeRateCriteria/>
            <div class="list">
                <table>
                    <thead>
                        <tr>

                   	        <g:sortableColumn property="code" title="Code" titleKey="exchangeCurrency.code" />

                   	        <g:sortableColumn property="name" title="Name" titleKey="exchangeCurrency.name" />

                   	        <g:sortableColumn property="decimals" title="Decimals" titleKey="exchangeCurrency.decimals" />

                   	        <g:sortableColumn property="autoUpdate" title="Auto Update" titleKey="exchangeCurrency.autoUpdate" />

                   	        <g:sortableColumn property="lastAutoCheck" title="Last Auto Check" titleKey="exchangeCurrency.lastAutoCheck" />

                   	        <g:sortableColumn property="lastAutoSucceeded" title="Last Auto Succeeded" titleKey="exchangeCurrency.lastAutoSucceeded" />

                            <g:exchangeRateHeading/>

                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${exchangeCurrencyInstanceList}" status="i" var="exchangeCurrencyInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                            <td><g:link action="show" id="${exchangeCurrencyInstance.id}">${fieldValue(bean:exchangeCurrencyInstance, field:'code')}</g:link></td>

                            <td>${fieldValue(bean:exchangeCurrencyInstance, field:'name')}</td>

                            <td>${fieldValue(bean:exchangeCurrencyInstance, field:'decimals')}</td>

                            <td><img src="${g.exchangeRateResource(dir: 'images', file: exchangeCurrencyInstance.autoUpdate.toString() + '.png')}" border="0"/></td>

                            <td><g:formatDate format="yyyy-MM-dd" date="${exchangeCurrencyInstance.lastAutoCheck}"/></td>

                            <td><g:if test="${exchangeCurrencyInstance.lastAutoSucceeded != null}"><img src="${g.exchangeRateResource(dir: 'images', file: exchangeCurrencyInstance.lastAutoSucceeded.toString() + '.png')}" border="0"/></g:if></td>

                            <g:exchangeRateData value="${exchangeCurrencyInstance.id}"/>

                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:exchangeCurrencyPaginate />
            </div>
        </div>
    </body>
</html>
