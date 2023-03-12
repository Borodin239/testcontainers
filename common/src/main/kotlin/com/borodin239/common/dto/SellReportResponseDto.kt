package com.borodin239.common.dto

import java.math.BigDecimal

data class SellReportResponseDto(
    val stocksId: Long,
    val login: String,
    val stocksName: String,
    val soldPrice: BigDecimal,
    val soldCount: Int = 0
)