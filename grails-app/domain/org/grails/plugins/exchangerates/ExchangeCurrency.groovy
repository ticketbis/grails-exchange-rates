package org.grails.plugins.exchangerates

import org.grails.plugins.exchangerates.*

class ExchangeCurrency {

    static hasMany = [exchangeRates: ExchangeRate]

    String code
    String name
    Byte decimals = 2
    Boolean autoUpdate
    Date lastAutoCheck
    Boolean lastAutoSucceeded
    Date dateCreated
    Date lastUpdated

    static mapping = {
        columns {
            code index: "currency_code_idx"
        }
    }

    static constraints = {
        code(blank:false, unique: true, matches:"[A-Z][A-Z][A-Z]")
        name(blank:false, size:1..100)
        decimals(range:0..3)
        lastAutoCheck(nullable:true, validator: {val, obj ->
                if (val == null) return true
                def cal = Calendar.getInstance()
                cal.setTime(val)
                return (cal.get(Calendar.HOUR_OF_DAY) == 0 && cal.get(Calendar.MINUTE) == 0 && cal.get(Calendar.SECOND) == 0 && cal.get(Calendar.MILLISECOND) == 0)
            })
        lastAutoSucceeded(nullable:true, validator: {val, obj ->
                if (obj.lastAutoCheck == null && val == false) {
                    obj.lastAutoSucceeded = null
                    return true
                }

                return ((obj.lastAutoCheck == null && val == null) || (obj.lastAutoCheck != null && val != null))
            })
    }
}
