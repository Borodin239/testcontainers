package com.borodin239.market.domain

import org.springframework.data.annotation.Id
import java.math.BigDecimal

data class Stocks(
    val stocksName: String,
    val marketPlace: String,
    val count: Int,
    val sellPrice: BigDecimal,
    @Id
    var id: Long = 0
)