package account.controller

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.testcontainers.containers.FixedHostPortGenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import javax.transaction.Transactional

@Testcontainers
@SpringBootTest
@Transactional
class AccountControllerIntegrityTests {
    @Autowired
    lateinit var controller: AccountController
    private val rest = TestRestTemplate()
    private var accountId: Long = 0

    @Container
    private val market = FixedHostPortGenericContainer<FixedHostPortGenericContainer<*>>("market:1.0-SNAPSHOT")
        .withFixedExposedPort(8080, 8080)
        .withExposedPorts(8080)

    @BeforeEach
    fun beforeTest() {
        market.start()
        createCompany(companyName, 10000, start_stock_price)
        accountId = controller.createAccount(start_balance).id
    }

    @AfterEach
    fun afterTest() {
        market.stop()
    }

    @Test
    fun testRegister() {
        with(controller.getAccount(accountId)) {
            assertEquals(start_balance, balance)
            assertTrue(stocks.isEmpty())
        }
    }

    @Test
    fun testAddMoney() {
        with(controller.addMoney(accountId, 1000.0)) {
            assertEquals(start_balance + 1000, balance)
            assertTrue(stocks.isEmpty())
        }
    }

    @Test
    fun testBuyStocks() {
        with(controller.buyStocks(accountId, companyName, 10)) {
            assertEquals(start_balance - 10 * start_stock_price, balance)
            assertEquals(10, stocks[companyName])
        }
        assertEquals(10 * start_stock_price, controller.sumStockBalance(accountId))
    }

    @Test
    fun testBuyStocksAfterPriceChange() {
        controller.buyStocks(accountId, companyName, 10)
        changePrice(companyName, 50.0)
        with(controller.buyStocks(accountId, companyName, 10)) {
            assertEquals(start_balance - 10 * start_stock_price - 10 * 50, balance)
            assertEquals(20, stocks[companyName])
        }
        assertEquals(20 * 50.0, controller.sumStockBalance(accountId))
    }

    @Test
    fun testBuySellIncrease() {
        controller.buyStocks(accountId, companyName, 10)
        changePrice(companyName, 50.0)
        with(controller.sellStocks(accountId, companyName, 10)) {
            assertEquals(start_balance - 10 * start_stock_price + 10 * 50, balance)
            assertEquals(0, stocks[companyName])
        }
        assertEquals(0.0, controller.sumStockBalance(accountId))
    }

    private fun createCompany(name: String, stocks: Int, price: Double) {
        rest.postForObject(
            "${MARKET_HOST}/create-company?name=$name&stocks=$stocks&price=$price",
            null, String::class.java
        )
    }

    private fun changePrice(name: String, price: Double) {
        rest.postForObject(
            "${MARKET_HOST}/update-price/$name?price=$price",
            null, String::class.java
        )
    }

    companion object {
        private const val MARKET_HOST = "http://localhost:8080/market"
        const val start_balance = 1000.0
        const val start_stock_price = 20.0
        const val companyName = "MegaCompany"
    }
}