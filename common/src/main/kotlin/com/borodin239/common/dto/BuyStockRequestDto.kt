package com.borodin239.common.dto

import java.math.BigDecimal

data class BuyStockRequestDto(
    val stocksName: String,
    val orderPrice: BigDecimal,
    val count : Int = 0
)