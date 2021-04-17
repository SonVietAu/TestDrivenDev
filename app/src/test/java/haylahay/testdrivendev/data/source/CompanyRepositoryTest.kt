package haylahay.testdrivendev.data.source

import haylahay.testdrivendev.data.source.remote.YahooFinanceStockMarketDS
import junit.framework.TestCase

class CompanyRepositoryTest : TestCase() {

    private lateinit var companyRepository: CompanyRepository

    public override fun setUp() {
        super.setUp()
        companyRepository = CompanyRepository(YahooFinanceStockMarketDS)
    }

    public override fun tearDown() {}


}