package haylahay.testdrivendev.ui.main.view.custom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import haylahay.testdrivendev.R
import haylahay.testdrivendev.data.Company

class CompaniesRVAdapter(var companies: List<Company>) : RecyclerView.Adapter<CompanyVH>() {

    override fun getItemCount(): Int = companies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyVH {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.company_vh, parent, false)
        return CompanyVH(itemView)
    }

    override fun onBindViewHolder(holder: CompanyVH, position: Int) {
        holder.bind(companies[position])
    }

}

class CompanyVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(company: Company) {

    }
}