package com.borodin239.client.dto

import java.math.BigDecimal

data class AddMoneyRequestDto(
    val login: String,
    val money: BigDecimal
)