class ExchangeRate {

    static belongsTo = ExchangeCurrency

    ExchangeCurrency currency
    Date validFrom
    BigDecimal rate
    Date dateCreated
    Date lastUpdated

    static constraints = {
        validFrom(unique: 'currency', validator: {val, obj ->
                if (val == null) return true
                def cal = Calendar.getInstance()
                cal.setTime(val)
                return (cal.get(Calendar.HOUR_OF_DAY) == 0 && cal.get(Calendar.MINUTE) == 0 && cal.get(Calendar.SECOND) == 0 && cal.get(Calendar.MILLISECOND) == 0)
            })
        rate(scale:6, min:0.000001)
    }
}
