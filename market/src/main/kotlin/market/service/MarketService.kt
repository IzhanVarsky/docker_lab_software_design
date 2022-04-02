package market.service

import market.entity.Company
import market.repository.CompanyRepository
import org.springframework.stereotype.Service

@Service
class MarketService(private val companyRepository: CompanyRepository) {
    fun addCompany(company: Company): Company {
        if (companyRepository.findByName(company.name!!).isPresent) {
            throw RuntimeException("Company already exists")
        }
        return company.saveNonAsserted()
    }

    fun getCompany(name: String): Company = companyRepository.findByName(name)
        .orElseThrow { RuntimeException("Company not found") }

    fun addStocks(name: String, stocks: Int): Company =
        getCompany(name).apply { addStocks(stocks) }.saveNonAsserted()

    fun buyStocks(name: String, stocks: Int): Company =
        getCompany(name).apply { buyStocks(stocks) }.saveNonAsserted()

    fun updateStockPrice(name: String, price: Double): Company =
        getCompany(name).apply { stockPrice = price }.saveNonAsserted()

    private fun Company.saveNonAsserted(): Company = companyRepository.save(this)

    val companies: List<Company>
        get() = companyRepository.findAll()
}
