package account.service

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@Service
class MarketService(restTemplateBuilder: RestTemplateBuilder) {
    private val rest: RestTemplate = restTemplateBuilder.build()

    fun buyStocks(name: String, stocks: Int) {
        rest.postForObject(
            "$MARKET_HOST/buy-stocks/$name?stocks=$stocks",
            null, String::class.java
        )
    }

    fun sellStocks(name: String, stocks: Int) {
        rest.postForObject(
            "$MARKET_HOST/add-stocks/$name?stocks=$stocks",
            null, String::class.java
        )
    }

    fun getStockPrice(name: String): Double =
        getCompanyByName(name)["stockPrice"] as Double

    fun getAvailableStocksCount(name: String): Int =
        getCompanyByName(name)["stocks"] as Int

    private fun getCompanyByName(name: String): Map<String, *> =
        rest.getForObject<Map<String, *>>("$MARKET_HOST/get-company/$name", HashMap::class.java)

    companion object {
        private const val MARKET_HOST = "http://localhost:8080/market"
    }
}