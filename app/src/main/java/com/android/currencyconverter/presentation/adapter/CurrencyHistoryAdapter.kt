package com.android.currencyconverter.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.currencyconverter.R
import com.android.currencyconverter.data.CurrencyHistory
import com.android.currencyconverter.databinding.ListOfCurrencyHistoryBinding

class CurrencyHistoryAdapter(private val list: List<CurrencyHistory>) :
    RecyclerView.Adapter<CurrencyHistoryAdapter.ViewModel>() {

    inner class ViewModel(item: View) : RecyclerView.ViewHolder(item) {
        var binding = ListOfCurrencyHistoryBinding.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModel {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_of_currency_history, parent, false)
        return ViewModel(view)
    }

    override fun onBindViewHolder(holder: ViewModel, position: Int) {
        var currency = list[position]
        with(holder.binding) {
            firstCurrency.text = "${currency.from}  ->  ${currency.rate}   ${currency.to}"

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}