package com.borodin239.market.dto

data class UpdateStocksCountRequestDto(
    val stocksName: String,
    val count: Int = 0
)