

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title><g:message code="exchangeCurrency.show" default="Show Exchange Currency" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/', absolute: true)}"><g:message code="home" default="Home" /></a></span>
            <g:exchangeRateMenuButton/>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="exchangeCurrency.list" default="Exchange Currency List" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="exchangeCurrency.new" default="New Exchange Currency" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="exchangeCurrency.show" default="Show Exchange Currency" /></h1>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="exchangeCurrency.id" default="Id" />:</td>

                            <td valign="top" class="value">${fieldValue(bean:exchangeCurrencyInstance, field:'id')}</td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="exchangeCurrency.code" default="Code" />:</td>

                            <td valign="top" class="value">${fieldValue(bean:exchangeCurrencyInstance, field:'code')}</td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="exchangeCurrency.name" default="Name" />:</td>

                            <td valign="top" class="value">${fieldValue(bean:exchangeCurrencyInstance, field:'name')}</td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="exchangeCurrency.decimals" default="Decimals" />:</td>

                            <td valign="top" class="value">${fieldValue(bean:exchangeCurrencyInstance, field:'decimals')}</td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="exchangeCurrency.autoUpdate" default="Auto Update" />:</td>

                            <td valign="top" class="value"><img src="${g.exchangeRateResource(dir: 'images', file: exchangeCurrencyInstance.autoUpdate.toString() + '.png')}" border="0"/></td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="exchangeCurrency.lastAutoCheck" default="Last Auto Check" />:</td>

                            <td valign="top" class="value"><g:formatDate format="yyyy-MM-dd" date="${exchangeCurrencyInstance.lastAutoCheck}"/></td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="exchangeCurrency.lastAutoSucceeded" default="Last Auto Succeeded" />:</td>

                            <td valign="top" class="value"><g:if test="${exchangeCurrencyInstance.lastAutoSucceeded != null}"><img src="${g.exchangeRateResource(dir: 'images', file: exchangeCurrencyInstance.lastAutoSucceeded.toString() + '.png')}" border="0"/></g:if></td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="exchangeCurrency.dateCreated" default="Date Created" />:</td>

                            <td valign="top" class="value">${fieldValue(bean:exchangeCurrencyInstance, field:'dateCreated')}</td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="exchangeCurrency.lastUpdated" default="Last Updated" />:</td>

                            <td valign="top" class="value">${fieldValue(bean:exchangeCurrencyInstance, field:'lastUpdated')}</td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="exchangeCurrency.version" default="Version" />:</td>

                            <td valign="top" class="value">${fieldValue(bean:exchangeCurrencyInstance, field:'version')}</td>

                        </tr>

                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <input type="hidden" name="id" value="${exchangeCurrencyInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="Edit" value="${message(code:'edit', 'default':'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('${message(code:'delete.confirm', 'default':'Are you sure?')}');" action="Delete" value="${message(code:'delete', 'default':'Delete')}" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
