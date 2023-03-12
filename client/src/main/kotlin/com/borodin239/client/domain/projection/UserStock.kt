package com.borodin239.client.domain.projection

import java.math.BigDecimal

data class UserStock(
    val stock: String,
    val marketPlace: String,
    val sellPrice: BigDecimal,
    val count: Int = 0,
)