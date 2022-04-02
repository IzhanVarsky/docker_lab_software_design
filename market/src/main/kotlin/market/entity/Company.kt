package market.entity

import javax.persistence.GeneratedValue
import java.lang.RuntimeException
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Company(var name: String? = null, var stocks: Int = 0, var stockPrice: Double = 0.0) {
    @Id
    @GeneratedValue
    var id: Long = 0L

    fun addStocks(stocks: Int) {
        this.stocks += stocks
    }

    fun buyStocks(stocks: Int) {
        if (this.stocks < stocks) {
            throw RuntimeException("Not enough stocks")
        }
        this.stocks -= stocks
    }
}