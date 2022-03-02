package com.android.currencyconverter.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.currencyconverter.R
import com.android.currencyconverter.data.CurrencyHistory
import com.android.currencyconverter.presentation.adapter.CurrencyHistoryAdapter
import com.android.currencyconverter.presentation.base.BaseFragment
import com.android.currencyconverter.utils.getErrorMessageFromCode
import com.android.currencyconverter.utils.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.currency_detail_fragment.*

@AndroidEntryPoint
class CurrencyDetailFragment : BaseFragment() {

    private lateinit var selectedFromCurrency: String
    private lateinit var selectedToCurrency: String

    private lateinit var adapter: CurrencyHistoryAdapter

    private val args: CurrencyDetailFragmentArgs by navArgs()
    private val viewModel: CurrencyDetailViewModel by viewModels()

    override fun observeViewModel() {
        observe(viewModel.rates, ::populateTheUI)
        observe(viewModel.errorLiveData, ::showError)
        observe(viewModel.currencyHistory, ::updateListWithHistory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedFromCurrency = args.fromCurrency.toString()
        selectedToCurrency = args.toCurrency.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.currency_detail_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        viewModel.getHistoryRatesForGivenCurrency(selectedFromCurrency, selectedToCurrency)
        with(currency_history_list) {
            val divider = DividerItemDecoration(
                context,
                LinearLayoutManager(context).orientation
            )
            layoutManager = LinearLayoutManager(activity?.applicationContext)
            addItemDecoration(divider)
        }
    }

    private fun updateListWithHistory(it: List<CurrencyHistory>) {
        progressBar.visibility = View.GONE
        adapter = CurrencyHistoryAdapter(it)
        currency_history_list.adapter = adapter
    }

    private fun showError(errorCode: Int) {
        Toast.makeText(
            activity,
            context?.let { getErrorMessageFromCode(it, errorCode) },
            Toast.LENGTH_LONG
        ).show()
    }

    @SuppressLint("SetTextI18n")
    private fun populateTheUI(hashMap: Map<String, Any>) {
        progressBar.visibility = View.GONE
        hashMap?.let {
            if (it.isNotEmpty()) {
                val keys = it.keys.toList()
                keys.forEach { key ->
                    val textView = TextView(context)
                    val padding = resources.getDimension(R.dimen._5dp).toInt()
                    textView.setPadding(padding, padding, padding, padding)
                    textView.text = " $key: ${it[key]}"
                    layout_rates_other_currencies.addView(textView)
                }

            }
        }

    }


}