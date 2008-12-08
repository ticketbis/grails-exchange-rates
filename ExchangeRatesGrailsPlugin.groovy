class ExchangeRatesGrailsPlugin {
    def version = 1.0
    def dependsOn = [:]

    // TODO Fill in these fields
    def author = "Paul Fernley"
    def authorEmail = "paul@pfernley.orangehome.co.uk"
    def title = "Foreign currency exchange rates using Yahoo!"
    def description = '''\
The exchange-rates plugin will either retrieve foreign currency exchange rates
dynamically from Yahoo! or can store rates in the database on a day-by-day,
currency-by-currency basis retrieving the rate from Yahoo! the first time each
day that a conversion is requested for a given currency. A full set of CRUD
screens is included together with a cache statistics screen (at a URL such as
http://myServer/myApp/exchangeRate/cache) and a test screen for checking that
the system is working (at a URL such as http://myServer/myApp/exchangeRate/test).
The screens assume you are using a layout called main. Your application must
have Internet access for the exchange-rates plugin to function.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/ExchangeRates+Plugin"

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }
   
    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)		
    }

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional)
    }
	                                      
    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }
	
    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
