package org.grails.plugins.exchangerates

class TestData {
	String fromCode
    String toCode
    Date date
    BigDecimal amount
    BigDecimal dailyRate
    BigDecimal dailyResult
    BigDecimal dynamicRate
    BigDecimal dynamicResult
    Boolean newlyCreated = true
}

