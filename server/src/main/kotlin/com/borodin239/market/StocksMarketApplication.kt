package com.borodin239.market

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StocksMarketApplication

fun main(args: Array<String>) {
    runApplication<StocksMarketApplication>(*args)
}
