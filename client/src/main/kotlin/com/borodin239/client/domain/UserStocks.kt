package com.borodin239.client.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("user_stocks")
data class UserStocks(
    @Id
    var stocksId: Long,
    var userId: Long,
    var count: Int,
    var updated: LocalDateTime
)