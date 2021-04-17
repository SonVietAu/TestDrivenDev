package haylahay.testdrivendev.ui.main

import android.util.Log
import androidx.lifecycle.*
import haylahay.testdrivendev.data.Company
import haylahay.testdrivendev.data.CompanyPrice
import haylahay.testdrivendev.data.source.CompanyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val companyRepository: CompanyRepository) : ViewModel() {
    // TODO: Implement the ViewModel

    private val _companiesMLD = MutableLiveData<List<Company>>()
    val companiesLD: LiveData<List<Company>> = _companiesMLD

    private val _newCompanyMLD = MutableLiveData<Company?>()
    val newCompanyLD: LiveData<Company?> = _newCompanyMLD

    private val _newCompanyPriceMLD = MutableLiveData<CompanyPrice?>()
    val newCompanyPriceLD: LiveData<CompanyPrice?> = _newCompanyPriceMLD

    fun loadCompanies() {

        viewModelScope.launch(Dispatchers.IO) {
            _companiesMLD.postValue(companyRepository.getCompanies())
        }

    }

    fun searchForCompany(stockIndex: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _newCompanyMLD.postValue(companyRepository.getNewCompany(stockIndex))
            Log.d("Sticky", "ran searchForCompany")
        }
    }

    fun addCompany(company: Company) {
        viewModelScope.launch(Dispatchers.IO) {
            companyRepository.insertCompany(company)
            loadCompanies()
        }
    }

    fun getNewPrice(companyPrice: CompanyPrice) {
        if (companiesLD.value?.firstOrNull { it.stockIndex == companyPrice.stockIndex } == null)
            throw Exception("Please use a CompanyPrice for a company from the added companies")

        viewModelScope.launch(Dispatchers.IO) {
            val newPrice = companyRepository.getRealTimePrice(companyPrice.stockIndex)
            if (newPrice != null) {
                companyPrice.price = newPrice
                _newCompanyPriceMLD.postValue(companyPrice)
            } else
                _newCompanyPriceMLD.postValue(null)

        }
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory (
    private val companyRepository: CompanyRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (MainViewModel(companyRepository) as T)
}