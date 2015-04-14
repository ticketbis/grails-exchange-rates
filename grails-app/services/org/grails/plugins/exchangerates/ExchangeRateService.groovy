package org.grails.plugins.exchangerates

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.codehaus.groovy.grails.commons.GrailsServiceClass
import org.codehaus.groovy.grails.commons.ServiceArtefactHandler
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.grails.plugins.exchangerates.*
import org.springframework.transaction.annotation.Transactional

class ExchangeRateService {

    static transactional = false

    private static String baseCode
    private static Boolean futureAllowed
    private static final currencies = [:]
    private static final cacheEntrySize = 32L     // Approximate size of an entry (key and value) in the cache
    private static cache = new LinkedHashMap((int) 16, (float) 0.75, (boolean) true)
    private static long maxCacheSize = 8L * 1024L // Cache size in KB (default is 8kb)
    private static long currentCacheSize = 0L
    private static long cacheHits = 0L
    private static long cacheMisses = 0L

    def convertDaily(value, from, to, date = null) {

        // Grab the exchange rate and decimal places involved
        def rate = dailyRate(from, to, date)
        def decs = getCurrency(to)?.decimals

        // If the rate is unavailable or the currency record cannot be found, return null
        if (!rate || decs == null) return null

        return round(value * rate, decs)
    }

    def dailyRate(from, to, date = null) {

        // Check the parameters
        if (!(from ==~ /[A-Z][A-Z][A-Z]/) || !(to ==~ /[A-Z][A-Z][A-Z]/) || (date && !(date instanceof Date))) {
            return null
        }

        // Default to today if required
        if (!date) date = new Date()

        // Ensure the date has no time portion
        fixDate(date)

        // Grab the exchange rates involved
        def fromRate = getRate(from, date)
        def toRate = getRate(to, date)

        // If either rate is unavailable return null
        if (!fromRate || !toRate) return null

        return toRate / fromRate
    }

    def convertDynamic(value, from, to) {

        // Grab the exchange rate and decimal places involved
        def rate = dynamicRate(from, to)
        def decs = getCurrency(to)?.decimals

        // If the rate is unavailable or the currency record cannot be found, return null
        if (!rate || decs == null) return null

        return round(value * rate, decs)
    }

    def dynamicRate(from, to) {

        // Check the parameters
        if (!(from ==~ /[A-Z][A-Z][A-Z]/) || !(to ==~ /[A-Z][A-Z][A-Z]/)) {
            return null
        }

        if (from == to) {
            return 1.0
        }

        def result = "http://finance.yahoo.com/d/quotes.csv?s=${from}${to}=X&f=b".toURL().getText()?.trim()

        try {
            return round(new BigDecimal(result), 6)
        } catch (NumberFormatException nfe) {}

        return null
    }

    def baseCurrencyCode() {
        if (!baseCode) {
            def val = ConfigurationHolder.config.exchangeRates.baseCurrencyCode
            if (val && val instanceof String && val ==~ /[A-Z][A-Z][A-Z]/) {
                baseCode = val
            } else {
                baseCode = "USD"
            }

            val = ConfigurationHolder.config.exchangeRates.future.rates
            if (val != null && val instanceof Boolean) {
                futureAllowed = val
            } else {
                futureAllowed = true
            }

            def size = ConfigurationHolder.config.exchangeRates.cache.size.kb
            if (size != null && size instanceof Integer && size >= 0 && size <= 1024 * 1024) {
                maxCacheSize = size * 1024L
            }

            if (ExchangeCurrency.count() == 0) {
                ExchangeCurrency.withTransaction {
                    def list = new CurrencyInitialData().getCurrencies()
                    list.each {
                        new ExchangeCurrency(it).save()
                    }

                    def cur = ExchangeCurrency.findByCode(baseCode)
                    cur.autoUpdate = false
                    cur.addToExchangeRates(new ExchangeRate([validFrom: fixDate(new Date(0L)), rate: 1.0]))
                    cur.save()
                }
            }
        }

        return baseCode
    }

    def decimalPlacesFor(code) {
        return getCurrency(code)?.decimals
    }

    def bulkUpdateRates() {

        def date = fixDate(new Date())

        ExchangeCurrency.findAllByAutoUpdate(true).each {
            getRate(it.code, date)
        }
    }

    def futureRatesAllowed() {

        // Ensure the system is initialized
        baseCurrencyCode()

        return futureAllowed
    }

    def resetCurrency(code) {
        synchronized (currencies) {
            currencies.remove(code)
        }

        resetThis(code)
    }

    def resetCurrencies() {
        synchronized (currencies) {
            currencies.clear()
        }

        resetAll()
    }

    def resetAll() {
        synchronized(cache) {
            cache.clear()
            currentCacheSize = 0L
            cacheHits = 0L
            cacheMisses = 0L
        }
    }

    def resetThis(String code) {
        synchronized(cache) {
            def entries = cache.entrySet().iterator()
            def entry
            while (entries.hasNext()) {
                entry = entries.next()
                if (entry.getKey().startsWith(code)) {
                    currentCacheSize -= cacheEntrySize
                    entries.remove()
                }
            }
        }
    }

    def statistics() {
        def stats = [:]
        synchronized (cache) {
            stats.max = maxCacheSize
            stats.size = currentCacheSize
            stats.count = cache.size()
            stats.hits = cacheHits
            stats.misses = cacheMisses
        }

        return stats
    }

    def hasPlugin(name) {
        return org.codehaus.groovy.grails.plugins.PluginManagerHolder.getPluginManager()?.hasGrailsPlugin(name)
    }

    def source(session, params, test){
        def result
        if (hasPlugin("drilldowns")) {
            def drilldownService = ((GrailsServiceClass) ApplicationHolder.getApplication().getArtefact(ServiceArtefactHandler.TYPE, "org.grails.plugins.drilldown.DrilldownService")).newInstance()
            result = drilldownService.source(session, params, test)
        }

        return result
    }

    // Clear any time portion
    def fixDate(date) {
        def cal = Calendar.getInstance()
        cal.setTime(date)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        return cal.getTime()
    }

    // Round a BigDecimal to a given precision
    def round(val, decs) {
        return val.setScale(decs, java.math.RoundingMode.HALF_UP)
    }

    private getCurrency(code) {
        def cur
        synchronized (currencies) {
            cur = currencies.get(code)
        }

        if (!cur) {
            cur = ExchangeCurrency.findByCode(code)
            if (cur) {
                def map = [:]
                map.id = cur.id
                map.decimals = cur.decimals
                map.autoUpdate = cur.autoUpdate
                map.lastAutoCheck = cur.lastAutoCheck
                synchronized (currencies) {
                    currencies.put(code, map)
                }

                cur = map
            }
        }

        return cur
    }

    private getRate(code, date) {

        // Ensure the currency definition is in the currencies map
        def map = getCurrency(code)
        if (!map) return null

        // Always return 1 for the base currency
        if (code == baseCurrencyCode()) return 1.0

        def key, value
        def today = fixDate(new Date())

        // We update today's exchange rate each day that a currency is used (regardless of whether it's todays rate they want)
        if (map.autoUpdate && (map.lastAutoCheck == null || map.lastAutoCheck.before(today))) {
            updateCurrency(code, map)
        }

        // Now set about getting what they actually asked for
        key = "${code}${date.getTime()}"
        if (maxCacheSize > 0) {
            synchronized (cache) {
                value = cache.get(key)

                if (value) {
                    cacheHits++
                } else {
                    cacheMisses++
                }
            }
        }

        if (!value) {
            def rates = ExchangeRate.findAll("from org.grails.plugins.exchangerates.ExchangeRate as x where x.currency.id = ? and x.validFrom <= ? order by x.validFrom desc", [map.id, date], [max: 1])
            if (!rates) return null
            value = rates[0].rate
            addToCache(key, value)
        }

        return value
    }

    @Transactional
    private updateCurrency(code, map) {
        // Ensure the system is initialized and note the base currency
        def base = baseCurrencyCode()

        def today = fixDate(new Date())

        // Start off by telling other people not to interfere
        synchronized (currencies) {
            map.lastAutoCheck = today
        }

        // Get the currency from the database
        def cur = ExchangeCurrency.get(map.id)

        // Double check no one has modified it behind our back
        if (cur && (cur.lastAutoCheck == null || cur.lastAutoCheck.before(today))) {
            cur.lastAutoCheck = today
            cur.lastAutoSucceeded = false
            if (cur.save()) {
                def value

                // See if a manually added rate exists for today
                def fxr = ExchangeRate.find("from org.grails.plugins.exchangerates.ExchangeRate as x where x.currency.id = ? and x.validFrom = ?", [map.id, today])

                // If there is such a record, grab its rate
                if (fxr) {
                    value = fxr.rate
                } else {    // Go get today's rate
                    value = dynamicRate(base, code)

                    // If there is a rate, update the currency and insert the new rate
                    if (value) {
                        cur = ExchangeCurrency.get(map.id)
                        if (cur) {
                            cur.lastAutoSucceeded = true
                            cur.addToExchangeRates(new ExchangeRate([validFrom: today, rate: value]))
                            if (cur.save()) {
                                resetThis(code) // Clear the cache for this currency's rates
                            } else {
                                value = null
                            }
                        } else {
                            value = null
                        }
                    }
                }

                // If there is a rate, add to the cache since we've got it here
                if (value) {
                    def key = "${code}${today.getTime()}"
                    addToCache(key, value)
                }
            }
        }
    }

    private addToCache(code, value) {
        if (maxCacheSize > 0) {
            synchronized (cache) {

                // Put it in the cache
                def prev = cache.put(code, value)

                // Another user may have inserted it while we weren't looking
                if (prev != null) currentCacheSize -= cacheEntrySize

                // Increment the cache size with our data
                currentCacheSize += cacheEntrySize

                // Adjust the cache size if required
                if (currentCacheSize > maxCacheSize) {
                    def entries = cache.entrySet().iterator()
                    def entry
                    while (entries.hasNext() && currentCacheSize > maxCacheSize) {
                        entry = entries.next()
                        currentCacheSize -= cacheEntrySize
                        entries.remove()
                    }
                }
            }
        }
    }
}
