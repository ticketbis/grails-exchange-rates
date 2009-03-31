import org.grails.plugins.exchangerates.*

class ExchangeRateTagLib {

    def exchangeRateService

    def exchangeRateMenuButton = {attrs, body ->
        if (exchangeRateService.hasPlugin("menus")) {
            out << '<span class="menuButton">'
            out << g.link(class: "menu", controller: "menu", action: "display") {
                g.message(code: "menu.display", default: "Menu")
            }
            out << '</span>'
        }
    }

    def exchangeRateHelpBalloons = {attrs, body ->
        if (exchangeRateService.hasPlugin("helpBalloons")) {
            out << g.helpBalloons(attrs)
        }
    }

    def exchangeRateHelpBalloon = {attrs, body ->
        if (exchangeRateService.hasPlugin("helpBalloons")) {
            out << g.helpBalloon(attrs)
        }
    }

    def exchangeRateHeading = {attrs, body ->
        if (exchangeRateService.hasPlugin("drilldowns")) {
            out << """<th>${g.message(code: "exchangeCurrency.rates", default: "Rates")}</th>"""
        }
    }

    def exchangeRateData = {attrs, body ->
        if (exchangeRateService.hasPlugin("drilldowns")) {
            out << """<td>${g.drilldown(controller: "exchangeRate", action: "list", value: attrs.value)}</td>"""
        }
    }

    def exchangeRateCriteria = {attrs, body ->
        if (exchangeRateService.hasPlugin("criteria")) {
            out << """<div class="criteria">\n"""
            out << g.criteria(attrs)
            out << """</div>\n"""
        }
    }

    def exchangeCurrencyPaginate = {attrs, body ->
        def count = (exchangeRateService.hasPlugin("criteria") || exchangeRateService.hasPlugin("drilldowns")) ? ExchangeCurrency.selectCount(session, params) : ExchangeCurrency.count()
        out << g.paginate(total: count)
    }

    def exchangeRatePaginate = {attrs, body ->
        def count = (exchangeRateService.hasPlugin("criteria") || exchangeRateService.hasPlugin("drilldowns")) ? ExchangeRate.selectCount(session, params) : ExchangeRate.count()
        out << g.paginate(total: count)
    }

    def exchangeRateDrilldownReturn = {attrs, body ->
        if (exchangeRateService.hasPlugin("drilldowns")) {
            out << g.drilldownReturn(attrs)
        }
    }
}
