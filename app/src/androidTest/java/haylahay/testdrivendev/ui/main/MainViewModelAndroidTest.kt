package haylahay.testdrivendev.ui.main

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import haylahay.testdrivendev.TDDApplication
import haylahay.testdrivendev.data.Company
import haylahay.testdrivendev.data.CompanyPrice
import haylahay.testdrivendev.data.source.CompanyRepository
import haylahay.testdrivendev.data.source.remote.YahooFinanceStockMarketDS
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Exception


@RunWith(AndroidJUnit4::class)
class MainViewModelAndroidTest : TestCase() {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var mainViewModel: MainViewModel

    lateinit var companyRepository: CompanyRepository

    val company1 = Company("Company 1", "COM1")
    val company2 = Company("Company 2", "COM2")

    @Before
    public override fun setUp() {
        super.setUp()
        val appContext = ApplicationProvider.getApplicationContext<TDDApplication>()
        companyRepository =
            CompanyRepository(YahooFinanceStockMarketDS, appContext.db.getCompaniesDao())

        val companies = companyRepository.getCompanies()
        companies.forEach {
            companyRepository.deleteCompany(it)
        }

        companyRepository.insertCompany(company1)
        companyRepository.insertCompany(company2)
        mainViewModel = MainViewModel(companyRepository)


        Log.d("Sticky", "Completed @Before")
    }

    @After
    public override fun tearDown() {
        Log.d("Sticky", "Completed @After")
    }

    @Test
    fun testLoadCompanies() {

        mainViewModel.companiesLD.observeForever(Observer {
            assertEquals(2, it.size)
            assertEquals(company1, it[0])
            assertEquals(company2, it[1])
            Log.d("Sticky", "tested loadCompanies() with ${it.size}")
        })


        runBlocking {

            assertEquals(2, companyRepository.getCompanies().size)

            launch(Dispatchers.IO) {
                mainViewModel.loadCompanies()
            }
        }
    }

    @Test
    fun testSearchCompany_For_Non_existence() =
        runBlocking {
            val stockIndex = "NONEXISTENCE_4321"
            mainViewModel.newCompanyLD.observeForever {
                assertNull(it)
                Log.d("Sticky", "tested testSearchCompany_For_Non_existence")
            }
            mainViewModel.searchForCompany(stockIndex)
            delay(10000) // To let coroutine complete
        }

    @Test
    fun testSearchCompany_For_Existing() {
        mainViewModel.newCompanyLD.observeForever {
            assertEquals("Apple Inc.", it!!.companyName)
            Log.d("Sticky", "tested testSearchCompany_For_Existing")
        }
        val stockIndex = "AAPL"
        mainViewModel.searchForCompany(stockIndex)

        Thread.sleep(10000) // To let coroutine complete
    }

    @Test
    fun testAddCompany() =
        runBlocking {
            val newCompany = Company("New Company", "NEWC")

            mainViewModel.companiesLD.observeForever {
                assertEquals(3, it.size)
                assertEquals(company1, it[0])
                assertEquals(company2, it[1])
                assertEquals(newCompany, it[2])
                Log.d("Sticky", "tested loadCompanies() with ${it.size}")
            }

            mainViewModel.addCompany(newCompany)
        }

    @Test
    fun testGetNewPrice() {

        runBlocking {

            try {
                mainViewModel.getNewPrice(CompanyPrice("NONEXISTENCE_4321"))
            } catch (ex: Exception) {
                assertEquals(
                    "Please use a CompanyPrice for a company from the added companies",
                    ex.message
                )
            }

            mainViewModel.newCompanyPriceLD.observeForever {
                assertNotNull(it)
                Log.d("Sticky", "tested testSearchCompany_For_Existing")
            }

            val stockIndex = "AAPL"

            mainViewModel.newCompanyLD.observeForever {
                if (it != null)
                    mainViewModel.addCompany(it)
                else
                    throw Exception("Company not found for stockIndex '$stockIndex'")
            }

            mainViewModel.searchForCompany(stockIndex)

            delay(10000)

            while (mainViewModel.companiesLD.value == null)
                delay(1000) // Ensure companies loaded
            val companyPrice = CompanyPrice(
                mainViewModel.companiesLD.value?.firstOrNull()?.stockIndex ?: "NONEXISTENCE_4321"
            )
            mainViewModel.getNewPrice(companyPrice)

            delay(10000) // To let coroutine complete
        }
    }
}