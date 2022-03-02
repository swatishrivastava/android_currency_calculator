import com.android.currencyconverter.data.response.CurrencyError
import com.android.currencyconverter.data.response.CurrencySymbolResponse
import com.android.currencyconverter.data.response.RatesResponse

fun getCurrencyMap(): Map<String, Any> {
    var hashMap = HashMap<String, String>()
    hashMap["AED"] = "4.129092"
    hashMap["CHF"] = "1.043536"
    hashMap["ALL"] = "121.043536"
    hashMap["INR"] = "84.816603"
    hashMap["USD"] = "14.043536"
    return hashMap
}

fun getCurrencyResponseTestData(): CurrencySymbolResponse {
    var hashMap = HashMap<String, String>()
    hashMap["AED"] = "4.166993"
    hashMap["CHF"] = "1.043536"
    hashMap["ALL"] = "121.043536"
    hashMap["INR"] = "1.043536"
    hashMap["USD"] = "14.043536"
    return CurrencySymbolResponse(true, hashMap, CurrencyError(0,"", ""))
}

fun getPopularCurrencyRatesTestData(): RatesResponse {
    var hashMap = HashMap<String, String>()
    hashMap["AED"] = "4.166993"
    hashMap["CHF"] = "1.043536"
    hashMap["ALL"] = "121.043536"
    hashMap["INR"] = "1.043536"
    hashMap["USD"] = "14.043536"
    return RatesResponse(
        "EUR", "2022-02-23", true,
        hashMap, true, 1645618566, CurrencyError(0,"", "")
    )
}


fun getEmptyPopularCurrencyRatesTestData(): RatesResponse {
    var hashMap = HashMap<String, String>()
    return RatesResponse(
        "EUR", "2022-02-23", true,
        hashMap, true, 1645618566, CurrencyError(0,"", "")
    )
}


fun getCurrencyResponseEmptyTestData(): CurrencySymbolResponse {
    var hashMap = HashMap<String, String>()
    return CurrencySymbolResponse(true, hashMap, CurrencyError(0,"", ""))
}