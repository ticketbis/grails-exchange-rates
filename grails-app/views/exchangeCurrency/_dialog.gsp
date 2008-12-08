                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="code"><g:message code="exchangeCurrency.code" default="Code" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:exchangeCurrencyInstance,field:'code','errors')}">
                                    <input type="text" id="code" name="code" value="${fieldValue(bean:exchangeCurrencyInstance,field:'code')}"/>&nbsp;<g:exchangeRateHelpBalloon code="exchangeCurrency.code" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="exchangeCurrency.name" default="Name" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:exchangeCurrencyInstance,field:'name','errors')}">
                                    <input type="text" maxlength="100" id="name" name="name" value="${fieldValue(bean:exchangeCurrencyInstance,field:'name')}"/>&nbsp;<g:exchangeRateHelpBalloon code="exchangeCurrency.name" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="decimals"><g:message code="exchangeCurrency.decimals" default="Decimals" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:exchangeCurrencyInstance,field:'decimals','errors')}">
                                    <g:select from="${0..3}" id="decimals" name="decimals" value="${exchangeCurrencyInstance?.decimals}" ></g:select>&nbsp;<g:exchangeRateHelpBalloon code="exchangeCurrency.decimals" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="autoUpdate"><g:message code="exchangeCurrency.autoUpdate" default="Auto Update" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:exchangeCurrencyInstance,field:'autoUpdate','errors')}">
                                    <g:checkBox name="autoUpdate" value="${exchangeCurrencyInstance?.autoUpdate}" ></g:checkBox>&nbsp;<g:exchangeRateHelpBalloon code="exchangeCurrency.autoUpdate" />
                                </td>
                            </tr>

                        </tbody>
                    </table>
                </div>
