<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title><g:message code="exchangeRate.list" default="Exchange Rate List" /></title>
        <g:exchangeRateHelpBalloons />
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}"><g:message code="home" default="Home" /></a></span>
            <g:exchangeRateMenuButton/>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="exchangeRate.new" default="New Exchange Rate" /></g:link></span>
        </div>
        <div class="body">
            <g:if test="${ddExchangeCurrency}">
                <h1><g:message code="exchangeRate.list.for" default="Exchange Rate List for" /> ${ddExchangeCurrency.name.encodeAsHTML()} <g:exchangeRateDrilldownReturn/></h1>
            </g:if>
            <g:else>
                <h1><g:message code="exchangeRate.list" default="Exchange Rate List" /></h1>
            </g:else>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:exchangeRateCriteria/>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="validFrom" title="Valid From" titleKey="exchangeRate.validFrom" />
                        
                   	        <g:sortableColumn property="rate" title="Rate" titleKey="exchangeRate.rate" />

                            <g:if test="${!ddExchangeCurrency}">
                                <th><g:message code="exchangeRate.currency" default="Currency"/></th>
                            </g:if>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${exchangeRateInstanceList}" status="i" var="exchangeRateInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${exchangeRateInstance.id}"><g:formatDate format="yyyy-MM-dd" date="${exchangeRateInstance.validFrom}"/></g:link></td>
                        
                            <td><g:formatNumber number="${exchangeRateInstance.rate}" format="0.000000"/></td>

                            <g:if test="${!ddExchangeCurrency}">
                                <td>${exchangeRateInstance.currency.name}</td>
                            </g:if>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:exchangeRatePaginate />
            </div>
        </div>
    </body>
</html>
