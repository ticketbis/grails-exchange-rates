                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="currency"><g:message code="exchangeRate.currency" default="Currency" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:exchangeRateInstance,field:'currency','errors')}">
                                    <g:select optionKey="id" optionValue="name" from="${ExchangeCurrency.list(sort: 'name')}" name="currency.id" value="${exchangeRateInstance?.currency?.id}" ></g:select>&nbsp;<g:exchangeRateHelpBalloon code="exchangeRate.currency" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="validFrom"><g:message code="exchangeRate.validFrom" default="Valid From" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:exchangeRateInstance,field:'validFrom','errors')}">
                                    <g:datePicker name="validFrom" value="${exchangeRateInstance?.validFrom}" precision="day" ></g:datePicker>&nbsp;<g:exchangeRateHelpBalloon code="exchangeRate.validFrom" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="rate"><g:message code="exchangeRate.rate" default="Rate" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:exchangeRateInstance,field:'rate','errors')}">
                                    <input type="text" id="rate" name="rate" value="${fieldValue(bean:exchangeRateInstance,field:'rate')}" />&nbsp;<g:exchangeRateHelpBalloon code="exchangeRate.rate" />
                                </td>
                            </tr>

                        </tbody>
                    </table>
                </div>
