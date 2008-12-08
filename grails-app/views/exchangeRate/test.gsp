<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title><g:message code="exchangeRate.test" default="Exchange Rate Test" /></title>
        <g:exchangeRateHelpBalloons />
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}"><g:message code="home" default="Home" /></a></span>
            <g:exchangeRateMenuButton/>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="exchangeRate.list" default="Exchange Rate List" /></g:link></span>
        </div>
        <div class="body">

            <h1><g:message code="exchangeRate.test" default="Exchange Rate Test" /></h1>
            <g:if test="${flash.message}">
                <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:form method="post" >
                <input type="hidden" name="dailyRate" value="${testDataInstance?.dailyRate}" />
                <input type="hidden" name="dailyResult" value="${testDataInstance?.dailyResult}" />
                <input type="hidden" name="dynamicRate" value="${testDataInstance?.dynamicRate}" />
                <input type="hidden" name="dynamicResult" value="${testDataInstance?.dynamicResult}" />
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="fromCode"><g:message code="exchangeRate.fromCode" default="From Currency" />:</label>
                                </td>
                                <td valign="top" class="value">
                                    <g:select optionKey="code" optionValue="name" from="${ExchangeCurrency.list(sort: 'name')}" name="fromCode" value="${testDataInstance?.fromCode}" ></g:select>&nbsp;<g:exchangeRateHelpBalloon code="exchangeRate.fromCode" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="toCode"><g:message code="exchangeRate.toCode" default="To Currency" />:</label>
                                </td>
                                <td valign="top" class="value">
                                    <g:select optionKey="code" optionValue="name" from="${ExchangeCurrency.list(sort: 'name')}" name="toCode" value="${testDataInstance?.toCode}" ></g:select>&nbsp;<g:exchangeRateHelpBalloon code="exchangeRate.toCode" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="date"><g:message code="exchangeRate.date" default="Conversion Date" />:</label>
                                </td>
                                <td valign="top" class="value">
                                    <g:datePicker name="date" value="${testDataInstance?.date}" precision="day" ></g:datePicker>&nbsp;<g:exchangeRateHelpBalloon code="exchangeRate.date" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="amount"><g:message code="exchangeRate.amount" default="Amount" />:</label>
                                </td>
                                <td valign="top" class="value">
                                    <input type="text" id="amount" name="amount" value="${testDataInstance?.amount}" />&nbsp;<g:exchangeRateHelpBalloon code="exchangeRate.amount" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name"><g:message code="exchangeRate.dailyRate" default="Daily Rate" />:</td>
                                <td valign="top" class="value"><g:formatNumber number="${testDataInstance?.dailyRate}" format="0.000000"/><g:if test="${!testDataInstance?.newlyCreated}">&nbsp;<g:exchangeRateHelpBalloon code="exchangeRate.dailyRate" /></g:if></td>

                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name"><g:message code="exchangeRate.dailyResult" default="Daily Result" />:</td>
                                <td valign="top" class="value">${testDataInstance?.dailyResult?.toPlainString()}<g:if test="${testDataInstance?.dailyResult != null}">&nbsp;<g:exchangeRateHelpBalloon code="exchangeRate.dailyResult" /></g:if></td>

                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name"><g:message code="exchangeRate.dynamicRate" default="Dynamic Rate" />:</td>
                                <td valign="top" class="value"><g:formatNumber number="${testDataInstance.dynamicRate}" format="0.000000"/><g:if test="${!testDataInstance?.newlyCreated}">&nbsp;<g:exchangeRateHelpBalloon code="exchangeRate.dynamicRate" /></g:if></td>

                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name"><g:message code="exchangeRate.dynamicResult" default="Dynamic Result" />:</td>
                                <td valign="top" class="value">${testDataInstance?.dynamicResult?.toPlainString()}<g:if test="${testDataInstance?.dynamicResult != null}">&nbsp;<g:exchangeRateHelpBalloon code="exchangeRate.dynamicResult" /></g:if></td>

                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="convert" value="${message(code:'exchangeRate.convert', 'default':'Convert')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
