package com.borodin239.market.mapper

import com.borodin239.common.dto.StocksResponseDto
import com.borodin239.market.domain.Stocks

object StocksResponseMapper {
    fun mapToStocksResponse(stocks: Stocks): StocksResponseDto = StocksResponseDto(
        stocks.stocksName,
        stocks.marketPlace,
        stocks.sellPrice.toString(),
        stocks.count
    )
}