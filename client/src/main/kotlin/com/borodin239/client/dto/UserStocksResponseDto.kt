package com.borodin239.client.dto

import java.math.BigDecimal

data class UserStocksResponseDto(
    val stockName: String,
    val marketPlace: String,
    val sellPrice: BigDecimal,
    val count: Int = 0,
)