package haylahay.testdrivendev.data.source

import android.util.Log
import androidx.test.core.app.ApplicationProvider
import haylahay.testdrivendev.TDDApplication
import haylahay.testdrivendev.data.Company
import haylahay.testdrivendev.data.source.remote.YahooFinanceStockMarketDS
import junit.framework.TestCase
import java.lang.Exception

class CompanyRepositoryAndroidTest : TestCase() {

    private lateinit var companyRepository: CompanyRepository

    public override fun setUp() {
        super.setUp()
        val appContext = ApplicationProvider.getApplicationContext<TDDApplication>()
        companyRepository = CompanyRepository(YahooFinanceStockMarketDS, appContext.db.getCompaniesDao())
    }

    public override fun tearDown() {}

    fun testGetNewCompany() {
        assertNull(companyRepository.getNewCompany("NONSENSE_STOCK_INDEX_4321"))
        assertEquals("Apple Inc.", companyRepository.getNewCompany("AAPL")!!.companyName)
        assertNotSame("Apple Inc.", companyRepository.getNewCompany("BHP")!!.companyName)

    }

    fun testGetRealTimePrice() {
        try {
            companyRepository.getRealTimePrice("NONSENSE_STOCK_INDEX_4321")
        } catch (ex: Exception) {
            assertEquals("Https Error: Internal Server Error", ex.message)
        }
        assertNotNull(companyRepository.getRealTimePrice("AAPL"))
        assertNotNull(companyRepository.getRealTimePrice("BHP"))
    }

    fun test_Local_DB_DML() {
        val company1 = Company("Company 1", "COM1")
        val company2 = Company("Company 2", "COM2")

        companyRepository.insertCompany(company1)
        companyRepository.insertCompany(company2)

        var companies = companyRepository.getCompanies()

        assertEquals(company1, companies[0])
        assertEquals(company2, companies[1])

        companyRepository.deleteCompany(company1)

        companies = companyRepository.getCompanies()
        assertEquals(company2, companies[0])

        companyRepository.deleteCompany(company2)

        companies = companyRepository.getCompanies()
        assertEquals(0, companies.size)
        Log.d("Sticky", "Local DB test completed")

    }
}