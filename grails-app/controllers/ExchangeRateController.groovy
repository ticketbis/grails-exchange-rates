import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler
import org.codehaus.groovy.grails.commons.ApplicationHolder

class ExchangeRateController {

    private static loaded = false

    def exchangeRateService
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    def allowedMethods = [delete:'POST', save:'POST', update:'POST', reset:'POST', convert:'POST']

    def list = {
        if (!loaded) {

            // Load up our properties file if needed
            if (exchangeRateService.hasPlugin("localizations")) {
                def test = message(code: "exchangeRate.id", default: "_missing!")
                if (test == "_missing!") {
                    Localization.loadPluginData("exchange-rates")
                }
            }

            // Ensure the database tables are loaded
            exchangeRateService.baseCurrencyCode()

            loaded = true
        }

        def max = 50
        def dflt = 20

        // If the settings plugin is available, try and use it for pagination
        if (exchangeRateService.hasPlugin("settings")) {

            // This convolution is necessary because this plugin can't see the
            // domain classes of another plugin
            def setting = ((GrailsDomainClass) ApplicationHolder.getApplication().getArtefact(DomainClassArtefactHandler.TYPE, "Setting")).newInstance()
            max = setting.valueFor("pagination.max", max)
            dflt = setting.valueFor("pagination.default", dflt)
        }

        params.max = (params.max && params.max.toInteger() > 0) ? Math.min(params.max.toInteger(), max) : dflt
        if (!params.sort) {
            params.sort = "validFrom"
            params.order = "desc"
        }

        def lst
        def ddExchangeCurrency = exchangeRateService.source(session, params, "exchangeCurrency.list")

        if (exchangeRateService.hasPlugin("criteria") || exchangeRateService.hasPlugin("drilldowns")) {
            lst = ExchangeRate.selectList( session, params )
        } else {
            lst = ExchangeRate.list( params )
        }

        [ exchangeRateInstanceList: lst, ddExchangeCurrency: ddExchangeCurrency ]
    }

    def show = {
        def exchangeRateInstance = ExchangeRate.get( params.id )

        if(!exchangeRateInstance) {
            flash.message = "exchangeRate.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Exchange Rate not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ exchangeRateInstance : exchangeRateInstance ] }
    }

    def delete = {
        def exchangeRateInstance = ExchangeRate.get( params.id )
        if(exchangeRateInstance) {
            if (exchangeRateInstance.currency.code == exchangeRateService.baseCurrencyCode()) {
                flash.message = "exchangeRate.no.delete"
                flash.args = [exchangeRateInstance.currency.code]
                flash.defaultMessage = "${exchangeRateInstance.currency.code} is the base currency and its rate may not be deleted"
            } else {
                exchangeRateInstance.delete()
                exchangeRateService.resetThis(exchangeRateInstance.currency.code)
                flash.message = "exchangeRate.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Exchange Rate ${params.id} deleted"
            }

            redirect(action:list)
        }
        else {
            flash.message = "exchangeRate.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Exchange Rate not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def exchangeRateInstance = ExchangeRate.get( params.id )

        if(!exchangeRateInstance) {
            flash.message = "exchangeRate.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Exchange Rate not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ exchangeRateInstance : exchangeRateInstance ]
        }
    }

    def update = {
        def exchangeRateInstance = ExchangeRate.get( params.id )
        if(exchangeRateInstance) {
            def oldDate = exchangeRateInstance.validFrom
            exchangeRateInstance.properties = params
            def valid = !exchangeRateInstance.hasErrors()
            if (valid && exchangeRateInstance.currency.code == exchangeRateService.baseCurrencyCode() && exchangeRateInstance.rate != 1.000000) {
                valid = false
                exchangeRateInstance.rate = 1.000000
                exchangeRateInstance.errors.rejectValue("rate",
                    "exchangeRate.rate.no.change", [exchangeRateInstance.currency.code] as Object[],
                    "${exchangeRateInstance.currency.code} is the base currency and its single rate must be 1.0")
            }

            if (valid && !exchangeRateService.futureRatesAllowed() && exchangeRateInstance?.validFrom.after(exchangeRateService.fixDate(new Date()))) {
                valid = false
                exchangeRateInstance.validFrom = oldDate
                exchangeRateInstance.errors.rejectValue("validFrom",
                    "exchangeRate.rate.future.error", [] as Object[],
                    "Entry of future exchange rates is not allowed")
            }

            if (valid) valid = exchangeRateInstance.save()

            if (valid) {
                exchangeRateService.resetThis(exchangeRateInstance.currency.code)
                flash.message = "exchangeRate.updated"
                flash.args = [params.id]
                flash.defaultMessage = "Exchange Rate ${params.id} updated"
                redirect(action:show,id:exchangeRateInstance.id)
            } else {
                render(view:'edit',model:[exchangeRateInstance:exchangeRateInstance])
            }
        } else {
            flash.message = "exchangeRate.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Exchange Rate not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def exchangeRateInstance = new ExchangeRate()
        exchangeRateInstance.properties = params
        def cur = exchangeRateService.source(session, [controller: params.controller, action: "list"], "exchangeCurrency.list")
        if (cur && cur.code != exchangeRateService.baseCurrencyCode()) {
            exchangeRateInstance.currency = cur.code
        }
        
        return ['exchangeRateInstance':exchangeRateInstance]
    }

    def save = {
        def exchangeRateInstance = new ExchangeRate(params)
        def valid = !exchangeRateInstance.hasErrors()

        if (valid && exchangeRateInstance.currency.code == exchangeRateService.baseCurrencyCode()) {
            valid = false
            exchangeRateInstance.errors.rejectValue("currency",
                "exchangeRate.rate.no.insert", [exchangeRateInstance.currency.code] as Object[],
                "${exchangeRateInstance.currency.code} is the base currency and insert of new rates is not allowed")
            exchangeRateInstance.currency = null
        }

        if (valid && !exchangeRateService.futureRatesAllowed() && exchangeRateInstance?.validFrom.after(exchangeRateService.fixDate(new Date()))) {
            valid = false
            exchangeRateInstance.errors.rejectValue("validFrom",
                    "exchangeRate.rate.future.error", [] as Object[],
                    "Entry of future exchange rates is not allowed")
        }

        if (valid) valid = exchangeRateInstance.save()

        if(valid) {
            exchangeRateService.resetThis(exchangeRateInstance.currency.code)
            flash.message = "exchangeRate.created"
            flash.args = ["${exchangeRateInstance.id}"]
            flash.defaultMessage = "Exchange Rate ${exchangeRateInstance.id} created"
            redirect(action:show,id:exchangeRateInstance.id)
        } else {
            render(view:'create',model:[exchangeRateInstance:exchangeRateInstance])
        }
    }

    def cache = {
        return [stats: exchangeRateService.statistics()]
    }

    def reset = {
        exchangeRateService.resetAll()
        redirect(action:cache)
    }

    def test = {
        if (!loaded) {

            // Load up our properties file if needed
            if (exchangeRateService.hasPlugin("localizations")) {
                def test = message(code: "exchangeRate.id", default: "_missing!")
                if (test == "_missing!") {
                    Localization.loadPluginData("exchange-rates")
                }
            }

            // Ensure the database tables are loaded
            exchangeRateService.baseCurrencyCode()

            loaded = true
        }

        def testDataInstance = new TestData()
        testDataInstance.date = exchangeRateService.fixDate(new Date())
        testDataInstance.amount = 1
        flash.message = "exchangeRate.test.warning"
        flash.defaultMessage = "NOTE: This test facility will access the Internet."
        return ['testDataInstance':testDataInstance]
    }

    def convert = {
        def testDataInstance = new TestData()
        testDataInstance.fromCode = params.fromCode
        testDataInstance.toCode = params.toCode
        if (params.date_year && params.date_month && params.date_day) {
            def cal = Calendar.getInstance()
            cal.set(Calendar.YEAR, params.date_year.toInteger())
            cal.set(Calendar.MONTH, params.date_month.toInteger() - 1)
            cal.set(Calendar.DAY_OF_MONTH, params.date_day.toInteger())
            testDataInstance.date = cal.getTime()
        }
        if (params.amount) testDataInstance.amount = params.amount.toBigDecimal()
        testDataInstance.newlyCreated = false

        def valid = true
        def tst, fromDecs, toDecs

        if (valid && !testDataInstance.fromCode) {
            valid = false
            flash.message = "exchangeRate.test.no.fromCode"
            flash.defaultMessage = "[From Currency] cannot be blank"
        }

        if (valid) {
            tst = ExchangeCurrency.findByCode(testDataInstance.fromCode)
            if (tst) {
                fromDecs = tst.decimals
            } else {
                valid = false
                flash.message = "exchangeRate.test.bad.fromCode"
                flash.defaultMessage = "Invalid [From Currency]"
            }
        }

        if (valid && !testDataInstance.toCode) {
            valid = false
            flash.message = "exchangeRate.test.no.toCode"
            flash.defaultMessage = "[To Currency] cannot be blank"
        }

        if (valid) {
            tst = ExchangeCurrency.findByCode(testDataInstance.toCode)
            if (tst) {
                toDecs = tst.decimals
            } else {
                valid = false
                flash.message = "exchangeRate.test.bad.toCode"
                flash.defaultMessage = "Invalid [From Currency]"
            }
        }

        if (valid && testDataInstance.fromCode == testDataInstance.toCode) {
            valid = false
            flash.message = "exchangeRate.test.same.codes"
            flash.defaultMessage = "[From Currency] and [To Currency] cannot be the same"
        }

        if (valid) {
            if (testDataInstance.date == null) {
                testDataInstance.date = new Date()
            }

            testDataInstance.date = exchangeRateService.fixDate(testDataInstance.date)

            if (testDataInstance.amount == null) {
                testDataInstance.amount = 1
            }
        }

        if (valid) {
            try {
                testDataInstance.amount = testDataInstance.amount.setScale(fromDecs)
            } catch (ArithmeticException ae) {
                valid = false
                flash.message = "exchangeRate.test.bad.amount"
                flash.defaultMessage = "[Amount] has too may decimal places for the [From Currency]"
            }
        }

        if (valid) {
            testDataInstance.dailyRate = exchangeRateService.dailyRate(testDataInstance.fromCode, testDataInstance.toCode, testDataInstance.date)
            testDataInstance.dynamicRate = exchangeRateService.dynamicRate(testDataInstance.fromCode, testDataInstance.toCode)

            if (testDataInstance.dailyRate) {
                testDataInstance.dailyResult = exchangeRateService.round(testDataInstance.amount * testDataInstance.dailyRate, toDecs)
            }

            if (testDataInstance.dynamicRate) {
                testDataInstance.dynamicResult = exchangeRateService.round(testDataInstance.amount * testDataInstance.dynamicRate, toDecs)
            }
        }
        
        render(view:'test',model:[testDataInstance:testDataInstance])
    }
}