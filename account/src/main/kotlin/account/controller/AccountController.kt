package account.controller

import account.entity.Account
import account.service.AccountService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/accounts")
class AccountController(private val accountService: AccountService) {
    @PostMapping("/create-account")
    fun createAccount(@RequestParam balance: Double): Account =
        accountService.saveAccount(Account(balance))

    @PostMapping("/add-money/{id}")
    fun addMoney(@PathVariable id: Long, @RequestParam money: Double): Account =
        accountService.updMoney(id, money)

    @PostMapping("/withdraw-money/{id}")
    fun withdrawMoney(@PathVariable id: Long, @RequestParam money: Double): Account =
        accountService.updMoney(id, -money)

    @GetMapping("/get-account/{id}")
    fun getAccount(@PathVariable id: Long): Account = accountService.getAccount(id)

    @PostMapping("/buy-stocks/{id}")
    fun buyStocks(@PathVariable id: Long, @RequestParam company: String, @RequestParam stocks: Int): Account =
        accountService.buyStocks(id, company, stocks)

    @PostMapping("/sell-stocks/{id}")
    fun sellStocks(@PathVariable id: Long, @RequestParam company: String, @RequestParam stocks: Int): Account =
        accountService.sellStocks(id, company, stocks)

    @GetMapping("/sum-balance/{id}")
    fun sumStockBalance(@PathVariable id: Long): Double = accountService.sumStockBalance(id)
}