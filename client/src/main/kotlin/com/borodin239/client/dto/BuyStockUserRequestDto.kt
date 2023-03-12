package com.borodin239.client.dto

import com.borodin239.common.dto.BuyStockRequestDto

data class BuyStockUserRequestDto(
    val login: String,
    val id: Long,
    val request: BuyStockRequestDto
)