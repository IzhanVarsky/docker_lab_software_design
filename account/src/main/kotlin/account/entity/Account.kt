package account.entity

import javax.persistence.*

@Entity
class Account(var balance: Double = 0.0) {
    @Id
    @GeneratedValue
    var id: Long = 0L

    @ElementCollection
    @CollectionTable(
        joinColumns = [JoinColumn(name = "accountId", referencedColumnName = "id")]
    )
    @MapKeyColumn(name = "stock_name")
    var stocks: MutableMap<String, Int> = mutableMapOf()

    fun updateBalance(money: Double) {
        balance += money
    }

    fun updCompanyStocksAndBalance(companyName: String, stocksCount: Int, stockPrice: Double) {
        stocks[companyName] = stocks.getOrDefault(companyName, 0) + stocksCount
        updateBalance(-stocksCount * stockPrice)
    }
}