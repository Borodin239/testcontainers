package com.borodin239.market.dto

import java.math.BigDecimal

data class UpdateStocksPriceRequestDto(
    val stocksName: String,
    val newPrice: BigDecimal
)