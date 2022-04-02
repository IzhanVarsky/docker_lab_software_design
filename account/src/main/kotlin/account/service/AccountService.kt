package account.service

import account.entity.Account
import account.repository.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountRepository: AccountRepository, private val marketService: MarketService
) {
    fun saveAccount(account: Account) = account.save()

    fun updMoney(accountId: Long, money: Double): Account = getAccount(accountId).apply { updateBalance(money) }.save()

    fun buyStocks(accountId: Long, company: String, stocks: Int): Account {
        val availableStocksCount = marketService.getAvailableStocksCount(company)
        if (availableStocksCount < stocks) {
            throw RuntimeException("Company has only $availableStocksCount stocks, which is less than $stocks")
        }
        val account = getAccount(accountId)
        val stockPrice = marketService.getStockPrice(company)
        val purchasePrice = stockPrice * stocks
        if (account.balance < purchasePrice) {
            throw RuntimeException("Not enough money on account balance")
        }
        marketService.buyStocks(company, stocks)
        return account.apply { updCompanyStocksAndBalance(company, stocks, stockPrice) }.save()
    }

    fun sellStocks(accountId: Long, company: String, stocks: Int): Account {
        val account = getAccount(accountId)
        val availableStocks = account.stocks.getOrDefault(company, 0)
        if (availableStocks < stocks) {
            throw RuntimeException("Not enough stocks on account")
        }
        val stockPrice = marketService.getStockPrice(company)
        marketService.sellStocks(company, stocks)
        return account.apply { updCompanyStocksAndBalance(company, -stocks, stockPrice) }.save()
    }

    fun getAccount(accountId: Long): Account =
        accountRepository.findById(accountId).orElseThrow { RuntimeException("Account not found") }

    fun sumStockBalance(accountId: Long): Double =
        getAccount(accountId).stocks.map { (key, value) -> marketService.getStockPrice(key) * value }.sum()

    private fun Account.save(): Account = accountRepository.save(this)
}
