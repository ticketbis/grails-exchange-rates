package org.grails.plugins.exchangerates

import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.grails.plugins.exchangerates.*

class ExchangeCurrencyController {

    private static loaded = false

    def exchangeRateService

    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

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
            def setting = ((GrailsDomainClass) ApplicationHolder.getApplication().getArtefact(DomainClassArtefactHandler.TYPE, "org.grails.plugins.settings.Setting")).newInstance()
            max = setting.valueFor("pagination.max", max)
            dflt = setting.valueFor("pagination.default", dflt)
        }

        params.max = (params.max && params.max.toInteger() > 0) ? Math.min(params.max.toInteger(), max) : dflt
        params.sort = params.sort ?: "code"

        def lst
        if (exchangeRateService.hasPlugin("criteria") || exchangeRateService.hasPlugin("drilldowns")) {
            lst = ExchangeCurrency.selectList( session, params )
        } else {
            lst = ExchangeCurrency.list( params )
        }

        [ exchangeCurrencyInstanceList: lst ]
    }

    def show = {
        def exchangeCurrencyInstance = ExchangeCurrency.get( params.id )

        if(!exchangeCurrencyInstance) {
            flash.message = "exchangeCurrency.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Exchange Currency not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ exchangeCurrencyInstance : exchangeCurrencyInstance ] }
    }

    def delete = {
        def exchangeCurrencyInstance = ExchangeCurrency.get( params.id )
        if(exchangeCurrencyInstance) {
            if (exchangeCurrencyInstance.code == exchangeRateService.baseCurrencyCode()) {
                flash.message = "exchangeCurrency.no.delete"
                flash.args = [exchangeCurrencyInstance.code]
                flash.defaultMessage = "${exchangeCurrencyInstance.code} is the base currency and may not be deleted"
            } else {
                exchangeCurrencyInstance.delete()
                exchangeRateService.resetCurrency(exchangeCurrencyInstance.code)
                flash.message = "exchangeCurrency.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Exchange Currency ${params.id} deleted"
            }

            redirect(action:list)
        }
        else {
            flash.message = "exchangeCurrency.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Exchange Currency not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def exchangeCurrencyInstance = ExchangeCurrency.get( params.id )

        if(!exchangeCurrencyInstance) {
            flash.message = "exchangeCurrency.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Exchange Currency not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ exchangeCurrencyInstance : exchangeCurrencyInstance ]
        }
    }

    def update = {
        def exchangeCurrencyInstance = ExchangeCurrency.get( params.id )
        if (exchangeCurrencyInstance) {
            def oldCode = exchangeCurrencyInstance.code
            exchangeCurrencyInstance.properties = params
            def valid = !exchangeCurrencyInstance.hasErrors()
            if (valid && oldCode == exchangeRateService.baseCurrencyCode()) {
                if (oldCode != exchangeCurrencyInstance.code) {
                    valid = false
                    exchangeCurrencyInstance.code = oldCode
                    exchangeCurrencyInstance.errors.rejectValue("code",
                        "exchangeCurrency.code.no.change", [oldCode] as Object[],
                        "${oldCode} is the base currency and may not have its code changed")
                }

                if (exchangeCurrencyInstance.autoUpdate) {
                    valid = false
                    exchangeCurrencyInstance.autoUpdate = false
                    exchangeCurrencyInstance.errors.rejectValue("autoUpdate",
                        "exchangeCurrency.no.auto", [oldCode] as Object[],
                        "${oldCode} is the base currency and may not be set to auto update")
                }
            }

            if (valid) valid = exchangeCurrencyInstance.save()

            if (valid) {
                exchangeRateService.resetCurrency(oldCode)
                if (exchangeCurrencyInstance.code != oldCode) exchangeRateService.resetCurrency(exchangeCurrencyInstance.code)
                flash.message = "exchangeCurrency.updated"
                flash.args = [params.id]
                flash.defaultMessage = "Exchange Currency ${params.id} updated"
                redirect(action:show,id:exchangeCurrencyInstance.id)
            } else {
                render(view:'edit',model:[exchangeCurrencyInstance:exchangeCurrencyInstance])
            }
        } else {
            flash.message = "exchangeCurrency.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Exchange Currency not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def exchangeCurrencyInstance = new ExchangeCurrency()
        exchangeCurrencyInstance.properties = params
        return ['exchangeCurrencyInstance':exchangeCurrencyInstance]
    }

    def save = {
        def exchangeCurrencyInstance = new ExchangeCurrency(params)
        if(!exchangeCurrencyInstance.hasErrors() && exchangeCurrencyInstance.save()) {
            exchangeRateService.resetCurrency(exchangeCurrencyInstance.code)
            flash.message = "exchangeCurrency.created"
            flash.args = ["${exchangeCurrencyInstance.id}"]
            flash.defaultMessage = "Exchange Currency ${exchangeCurrencyInstance.id} created"
            redirect(action:show,id:exchangeCurrencyInstance.id)
        }
        else {
            render(view:'create',model:[exchangeCurrencyInstance:exchangeCurrencyInstance])
        }
    }
}