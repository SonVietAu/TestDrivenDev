package haylahay.testdrivendev.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import haylahay.testdrivendev.R
import haylahay.testdrivendev.TDDApplication
import haylahay.testdrivendev.data.source.CompanyRepository
import haylahay.testdrivendev.data.source.remote.YahooFinanceStockMarketDS
import haylahay.testdrivendev.ui.main.view.custom.CompaniesRVAdapter
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel by viewModels<MainViewModel> {
        MainViewModelFactory(CompanyRepository(YahooFinanceStockMarketDS, (this@MainFragment.requireActivity().application as TDDApplication).db.getCompaniesDao()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchBtn.setOnClickListener {
            // TODO: Create SearchCompanyDialogFragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.companiesLD.observe(this.viewLifecycleOwner) {
            Log.d("Sticky", "Loaded ${it.size} companies")

            if (companiesRV.adapter is CompaniesRVAdapter) {
                (companiesRV.adapter as CompaniesRVAdapter).companies = it
                companiesRV.adapter!!.notifyDataSetChanged()
            } else {
                companiesRV.adapter = CompaniesRVAdapter(it)
            }

        }

        viewModel.loadCompanies()
    }

}