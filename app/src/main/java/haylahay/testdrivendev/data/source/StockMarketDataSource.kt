package haylahay.testdrivendev.data.source

import haylahay.testdrivendev.data.*

interface StockMarketDataSource {
    fun getCompany(stockIndex: String): Company?
    fun getRealTimePrice(stockIndex: String): Float?
}