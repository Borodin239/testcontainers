package com.borodin239.common.dto

import java.math.BigDecimal

data class PaymentResponseDto(
    val stockId: Long,
    val stocksName: String,
    val stocksPrice: BigDecimal,
    val totalPrice: BigDecimal,
    val count: Int = 0,
)