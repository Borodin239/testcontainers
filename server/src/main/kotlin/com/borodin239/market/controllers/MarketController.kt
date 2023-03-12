package com.borodin239.market.controllers

import com.borodin239.market.dto.AddStocksRequestDto
import com.borodin239.market.dto.UpdateStocksCountRequestDto
import com.borodin239.market.dto.UpdateStocksPriceRequestDto
import com.borodin239.market.service.MarketService
import com.borodin239.common.dto.BuyStockRequestDto
import com.borodin239.common.dto.PaymentResponseDto
import com.borodin239.common.dto.SellReportResponseDto
import com.borodin239.common.dto.StocksResponseDto
import com.borodin239.common.dto.UserSellStockRequestDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/market")
class MarketController(@Autowired private val service: MarketService) {
    @PostMapping("/add")
    fun addStocks(
        @RequestHeader("marketId") marketId: String,
        @RequestBody dto: AddStocksRequestDto
    ): Mono<StocksResponseDto> {
        return service.addStocks(dto)
    }

    @PostMapping("/stock/update/count")
    fun updateStocksCount(
        @RequestHeader("marketId") marketId: String,
        @RequestBody dto: UpdateStocksCountRequestDto
    ): Mono<StocksResponseDto> {
        return service.updateStocksCount(dto)
    }

    @PostMapping("/stock/update/price")
    fun updateStocksCount(
        @RequestHeader("marketId") marketId: String,
        @RequestBody dto: UpdateStocksPriceRequestDto
    ): Mono<StocksResponseDto> {
        return service.updateStocksPrice(dto)
    }

    @GetMapping("/stocks")
    fun allStocks(): Flux<StocksResponseDto> {
        return service.getAllStocks()
    }

    @PostMapping("/stocks/buy")
    fun buyStock(@RequestBody dto: BuyStockRequestDto): Mono<PaymentResponseDto> {
        return service.buyStock(dto)
    }

    @PostMapping("/stocks/sell")
    fun sellStocks(@RequestBody dto: UserSellStockRequestDto): Mono<SellReportResponseDto> {
        return service.sellStocks(dto)
    }
}