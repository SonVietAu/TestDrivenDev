package haylahay.testdrivendev.data.source.remote

import android.util.Log
import haylahay.testdrivendev.data.Company
import haylahay.testdrivendev.data.source.StockMarketDataSource
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.HashMap
import javax.net.ssl.HttpsURLConnection

object YahooFinanceStockMarketDS : StockMarketDataSource {


    private val headersMap = HashMap<String, String>()

    init {
        headersMap.put("x-rapidapi-key", "514f86c718mshd3b7176c4460b60p1b78f6jsncc1ee050a660")
        headersMap.put("x-rapidapi-host", "stock-market-data.p.rapidapi.com")
        headersMap.put("useQueryString", true.toString())
    }

    val YAHOO_FINANCE_STOCK_MARKET_API_URL = "https://stock-market-data.p.rapidapi.com"

    override fun getCompany(stockIndex: String): Company? {

        val url = URL("$YAHOO_FINANCE_STOCK_MARKET_API_URL/stock/company-info?ticker_symbol=$stockIndex")

        val conn = url.openConnection() as HttpsURLConnection

        headersMap.forEach {
            conn.setRequestProperty(it.key, it.value)
        }

        conn.connect()

        val httpResponseCode = conn.responseCode

        if (httpResponseCode == HttpsURLConnection.HTTP_OK) {


            val reader = BufferedReader(InputStreamReader(conn.getInputStream()));
            val stringBuilder = StringBuilder();
            var line: String? = reader.readLine()
            while (line != null) {
                stringBuilder.append(line + "\n");
                line = reader.readLine()
            }
            Log.d("Sticky", "Server Response:$stringBuilder")
            val serverJson = JSONObject(stringBuilder.toString())
            Log.d("Sticky", "serverJson:$serverJson")
            if (serverJson.getString("status") == "success") {
                val companyProfileJson = serverJson.getJSONObject("company_profile")
                if (companyProfileJson.isNull("Company Name"))
                    return null
                else
                    return Company(companyProfileJson.getString("Company Name"), stockIndex)
            } else
                return null
        } else {
            throw Exception("Https Error: ${conn.responseMessage}")
        }
    }

    override fun getRealTimePrice(stockIndex: String): Float? {

        val url = URL("$YAHOO_FINANCE_STOCK_MARKET_API_URL/yfinance/price?ticker_symbol=$stockIndex")

        val conn = url.openConnection() as HttpsURLConnection

        headersMap.forEach {
            conn.setRequestProperty(it.key, it.value)
        }

        conn.connect()

        val httpResponseCode = conn.responseCode

        if (httpResponseCode == HttpsURLConnection.HTTP_OK) {

            val reader = BufferedReader(InputStreamReader(conn.getInputStream()));
            val stringBuilder = StringBuilder();
            var line: String? = reader.readLine()
            while (line != null) {
                stringBuilder.append(line + "\n");
                line = reader.readLine()
            }
            Log.d("Sticky", "Server Response:$stringBuilder")
            val serverJson = JSONObject(stringBuilder.toString())
            return if (serverJson.getString("status") == "success") {
                serverJson
                    .getJSONObject("price")
                    .getJSONObject("regularMarketOpen")
                    .getDouble("raw").toFloat()
            } else
                null
        } else {
            throw Exception("Https Error: ${conn.responseMessage}")
        }
    }

}