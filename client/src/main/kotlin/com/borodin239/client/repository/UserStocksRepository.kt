package com.borodin239.client.repository

import com.borodin239.client.domain.UserStocks
import com.borodin239.client.domain.projection.UserStock
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserStocksRepository : ReactiveCrudRepository<UserStocks, Long> {
    fun findUserStocksByUserId(id: Long?): Flux<UserStocks>

    @Query(
        "select * from user_stocks where user_id IN " +
                " (select users.id from users where login = :login) " +
                "AND stocks_id IN (select id from stocks where stocks.stocks_name = :stocksName)"
    )
    fun findUserStocksByLoginAndStocksName(
        @Param("login") login: String,
        @Param("stocksName") stocksName: String
    ): Mono<UserStocks>

    @Query(
        "select user_stocks.counts as count, stocks.stocks_name as stock, market_place_provider,sell_price from user_stocks " +
                " join stocks on user_stocks.stocks_id = stocks.id where user_stocks.user_id = $1"
    )
    fun findUserStocks(uid: Long): Flux<UserStock>

}