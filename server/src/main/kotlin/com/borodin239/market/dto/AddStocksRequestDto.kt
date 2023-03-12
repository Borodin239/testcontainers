package com.borodin239.market.dto

import java.math.BigDecimal

data class AddStocksRequestDto(
    val stocksName: String,
    val marketPlace: String,
    val sellPrice: BigDecimal,
    val count: Int = 0
)