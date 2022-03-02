package com.android.currencyconverter.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.currencyconverter.R
import com.android.currencyconverter.presentation.base.BaseFragment
import com.android.currencyconverter.utils.getErrorMessageFromCode
import com.android.currencyconverter.utils.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.currency_converter_fragment.*

const val DEFAULT_FIRST_CURRENCY = "AED"
const val DEFAULT_SECOND_CURRENCY = "INR"

@AndroidEntryPoint
class CurrencyConverterFragment : BaseFragment(), AdapterView.OnItemSelectedListener {
    private var selectCurrencyFrom: String = DEFAULT_FIRST_CURRENCY
    private var selectCurrencyTo: String = DEFAULT_SECOND_CURRENCY
    private var currencyList: List<String>? = null
    private val viewModel: CurrencyConverterViewModel by viewModels()

    override fun observeViewModel() {
        observe(viewModel.symbols, ::prepareCurrencySpinners)
        observe(viewModel.errorLiveData, ::showError)
        observe(viewModel.amount, ::showConvertedAmount)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.currency_converter_fragment, container, false)
    }


    override fun onStart() {
        super.onStart()
        viewModel.getCurrencySymbols()
        amountTextChangeHandling()
        handlingExchangeIconClick()
        detail_button.setOnClickListener {
            var action = CurrencyConverterFragmentDirections
                .actionToDetailFragment(selectCurrencyFrom, selectCurrencyTo)
            findNavController().navigate(action)
        }
    }

    private fun handlingExchangeIconClick() {
        exchange_icon.setOnClickListener {
            currencyList?.indexOf(selectCurrencyFrom)
                ?.let { it1 -> to_currency_spinner.setSelection(it1) }
            currencyList?.indexOf(selectCurrencyTo)
                ?.let { it1 -> from_currency_spinner.setSelection(it1) }
        }
    }

    private fun amountTextChangeHandling() {
        enter_amount_edittext.doOnTextChanged { text, start, before, count ->
            if (text?.isNotEmpty() == true) {
                viewModel.getConvertedAmount(selectCurrencyFrom, selectCurrencyTo, text.toString())
            } else {
                converted_amount_edittext.setText(R.string.zero_text)
            }
        }
    }

    private fun showConvertedAmount(amt: String) {
        converted_amount_edittext.setText(amt)

    }

    private fun showError(errorCode: Int) {
        Toast.makeText(
            activity,
            context?.let { it1 -> getErrorMessageFromCode(it1, errorCode) },
            Toast.LENGTH_LONG
        ).show()
        detail_button.isEnabled = false
    }

    private fun prepareCurrencySpinners(list: List<String>) {
        this.currencyList = list
        from_currency_spinner.onItemSelectedListener = this
        to_currency_spinner.onItemSelectedListener = this
        context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item,
                list,
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                from_currency_spinner.adapter = adapter
                to_currency_spinner.adapter = adapter
                to_currency_spinner.setSelection(list.indexOf("INR"))
            }
        }
    }


    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, id: Long) {
        if (parent?.id == R.id.from_currency_spinner) {
            selectCurrencyFrom = parent.getItemAtPosition(pos).toString()
            viewModel.getConvertedAmount(
                selectCurrencyFrom, selectCurrencyTo,
                enter_amount_edittext.text.toString()
            )
        }

        if (parent?.id == R.id.to_currency_spinner) {
            selectCurrencyTo = parent.getItemAtPosition(pos).toString()
            viewModel.getConvertedAmount(
                selectCurrencyFrom, selectCurrencyTo,
                enter_amount_edittext.text.toString()
            )

        }

    }


    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


}