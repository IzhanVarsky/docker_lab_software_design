package market.controller

import market.entity.Company
import market.service.MarketService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/market")
class MarketController(private val marketService: MarketService) {
    @PostMapping("/create-company")
    fun createCompany(@RequestParam name: String, @RequestParam stocks: Int, @RequestParam price: Double): Company =
        marketService.addCompany(Company(name, stocks, price))

    @PostMapping("/add-stocks/{name}")
    fun addStocks(@PathVariable name: String, @RequestParam stocks: Int): Company =
        marketService.addStocks(name, stocks)

    @GetMapping("/get-all-companies")
    fun getAllCompanies(): List<Company> = marketService.companies

    @GetMapping("/get-company/{name}")
    fun getCompany(@PathVariable name: String): Company = marketService.getCompany(name)

    @PostMapping("/buy-stocks/{name}")
    fun buyStocks(@PathVariable name: String, @RequestParam stocks: Int): Company =
        marketService.buyStocks(name, stocks)

    @PostMapping("/update-price/{name}")
    fun updateStockPrice(@PathVariable name: String, @RequestParam price: Double): Company =
        marketService.updateStockPrice(name, price)
}