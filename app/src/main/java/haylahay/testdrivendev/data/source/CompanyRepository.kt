package haylahay.testdrivendev.data.source

import haylahay.testdrivendev.data.CompaniesDao
import haylahay.testdrivendev.data.Company

class CompanyRepository(
    val stockMarketDataSource: StockMarketDataSource,
    val companiesDao: CompaniesDao,
) {

    fun getNewCompany(stockIndex: String): Company? = stockMarketDataSource.getCompany(stockIndex)
    fun getRealTimePrice(stockIndex: String): Float? = stockMarketDataSource.getRealTimePrice(stockIndex)

    // Can not see the need to test these two functions
    fun getCompanies(): List<Company> = companiesDao.getCompanies()
    fun insertCompany(company: Company) = companiesDao.insert(company)
    fun deleteCompany(company: Company) = companiesDao.delete(company)

}