package com.borodin239.client.controllers

import com.borodin239.client.domain.UserStocks
import com.borodin239.client.service.UserService
import com.borodin239.common.dto.BaseResponse
import com.borodin239.common.dto.StocksResponseDto
import com.borodin239.common.dto.UserSellStockRequestDto
import com.borodin239.client.dto.AddMoneyRequestDto
import com.borodin239.client.dto.BuyStockUserRequestDto
import com.borodin239.client.dto.RegisterUserRequestDto
import com.borodin239.client.dto.UserInfoDto
import com.borodin239.client.dto.UserStocksResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/stocks/client")
class UserController(@Autowired private val userService: UserService) {
    @PostMapping("user/new")
    fun registerUser(@RequestBody dto: RegisterUserRequestDto): Mono<UserInfoDto> {
        return userService.registerUser(dto)
    }

    @PostMapping("user/add-money")
    fun addMoneyToAccount(@RequestBody dto: AddMoneyRequestDto): Mono<BaseResponse> {
        return userService.addMoney(dto)
    }

    @PostMapping("user/buy-stocks")
    fun buyStocks(@RequestBody dto: BuyStockUserRequestDto): Mono<UserStocks> {
        return userService.buyStocks(dto)
    }

    @PostMapping("user/sell-stocks")
    fun sellStocks(@RequestBody dto: UserSellStockRequestDto): Mono<BaseResponse> {
        return userService.sellStocks(dto)
    }

    @GetMapping("user/stocks/all")
    fun getUserStocks(@RequestHeader("id") uid: Long): Flux<UserStocksResponseDto> {
        return userService.getUserStocks(uid)
    }

    @GetMapping("/stocks")
    fun getAllStocks(@RequestHeader("id") uid: Long): Flux<StocksResponseDto> {
        return userService.getAvailableStocks()
    }
}