package com.borodin239.client.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Table("users")
data class User(
    val login: String, val balance: BigDecimal, val registered: LocalDateTime,
    @Id
    var id: Long = 0
)