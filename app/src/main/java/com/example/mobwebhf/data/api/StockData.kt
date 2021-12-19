package com.example.mobwebhf.data.api

data class StockData (
    var query: StockQuery,
    var data: Map<String, Double>
        )

