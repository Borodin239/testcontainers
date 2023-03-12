package com.borodin239.market.service

import com.borodin239.common.exceptions.StockCompanyNotFoundException
import com.borodin239.common.exceptions.StocksIllegalRequestException
import com.borodin239.market.domain.Stocks
import com.borodin239.market.dto.AddStocksRequestDto
import com.borodin239.market.dto.UpdateStocksCountRequestDto
import com.borodin239.market.dto.UpdateStocksPriceRequestDto
import com.borodin239.market.mapper.StocksResponseMapper
import com.borodin239.market.repository.StocksRepository
import com.borodin239.common.dto.BuyStockRequestDto
import com.borodin239.common.dto.PaymentResponseDto
import com.borodin239.common.dto.SellReportResponseDto
import com.borodin239.common.dto.StocksResponseDto
import com.borodin239.common.dto.UserSellStockRequestDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal

@Service
class MarketService(@Autowired private val stocksRepository: StocksRepository) {
    companion object {
        fun createCompanyDoesNotExistMsg(name: String) = "Stock company $name does not exist."
    }

    fun addStocks(dto: AddStocksRequestDto): Mono<StocksResponseDto> {
        return try {
            val savedStocks: Mono<Stocks> = stocksRepository.save(
                Stocks(dto.stocksName, dto.marketPlace, dto.count, dto.sellPrice)
            )
            savedStocks.map(StocksResponseMapper::mapToStocksResponse)
        } catch (e: Exception) {
            throw StocksIllegalRequestException(e.message, e)
        }
    }

    fun updateStocksCount(dto: UpdateStocksCountRequestDto): Mono<StocksResponseDto> {
        if (dto.count < 0) {
            throw StocksIllegalRequestException("Stock count can't be negative")
        }
        return stocksRepository.findStocksByStocksName(dto.stocksName)
            .switchIfEmpty(
                Mono.error(
                    StockCompanyNotFoundException(createCompanyDoesNotExistMsg(dto.stocksName))
                )
            )
            .doOnNext { stocks ->
                stocksRepository.save(stocks.copy(count = stocks.count + dto.count)).subscribe()
            }
            .map(StocksResponseMapper::mapToStocksResponse)
    }

    fun updateStocksPrice(dto: UpdateStocksPriceRequestDto): Mono<StocksResponseDto> {
        if (dto.newPrice.signum() <= 0) {
            throw StocksIllegalRequestException("Stock price must be positive")
        }
        return stocksRepository.findStocksByStocksName(dto.stocksName)
            .switchIfEmpty(
                Mono.error(StockCompanyNotFoundException(createCompanyDoesNotExistMsg(dto.stocksName)))
            )
            .doOnNext { stocks ->
                stocksRepository.save(stocks.copy(sellPrice = dto.newPrice)).subscribe()
            }
            .map(StocksResponseMapper::mapToStocksResponse)
    }

    fun getAllStocks(): Flux<StocksResponseDto> = stocksRepository.findAll()
        .flatMap { stocks -> Mono.just(StocksResponseMapper.mapToStocksResponse(stocks)) }

    @Transactional
    fun buyStock(dto: BuyStockRequestDto): Mono<PaymentResponseDto> {
        return stocksRepository.findStocksByStocksName(dto.stocksName)
            .switchIfEmpty(
                Mono.error(
                    StockCompanyNotFoundException(createCompanyDoesNotExistMsg(dto.stocksName))
                )
            )
            .flatMap { stocks ->
                if (stocks.count < dto.count) {
                    return@flatMap Mono.error(
                        StocksIllegalRequestException("Not enough shares for stock ${dto.stocksName}. Actual: ${dto.count}. Required: ${stocks.count}")
                    )
                }
                if (stocks.sellPrice.subtract(dto.orderPrice).abs() > EPSILON_ERROR) {
                    return@flatMap Mono.error(
                        StocksIllegalRequestException("Stock price for ${dto.stocksName} has been changed. Actual price is ${stocks.sellPrice}")
                    )
                }
                stocksRepository.save(stocks.copy(count = stocks.count - dto.count)).subscribe()
                Mono.just(stocks)
            }
            .map { stocks ->
                PaymentResponseDto(
                    stocks.id,
                    dto.stocksName,
                    dto.orderPrice,
                    stocks.sellPrice.multiply(BigDecimal(dto.count)),
                    dto.count
                )
            }
    }

    @Transactional
    fun sellStocks(dto: UserSellStockRequestDto): Mono<SellReportResponseDto> {
        return stocksRepository.findStocksByStocksName(dto.stocksName)
            .switchIfEmpty(
                Mono.error(
                    StockCompanyNotFoundException(createCompanyDoesNotExistMsg(dto.stocksName))
                )
            )
            .doOnNext { stocks ->
                if (stocks.sellPrice.subtract(dto.orderPrice).abs() < EPSILON_ERROR) {
                    throw StocksIllegalRequestException(
                        String.format(
                            "Stock price for ${dto.stocksName} has been changed. Actual price is ${stocks.sellPrice}",
                        )
                    )
                }
                stocksRepository.save(stocks.copy(count = stocks.count + dto.count)).subscribe()
            }
            .map { stocks ->
                SellReportResponseDto(
                    stocks.id,
                    dto.login,
                    stocks.stocksName,
                    dto.orderPrice,
                    dto.count,
                )
            }
    }
}

private val EPSILON_ERROR = BigDecimal("1e-8")
