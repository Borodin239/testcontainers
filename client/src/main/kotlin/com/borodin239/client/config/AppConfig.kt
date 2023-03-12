package com.borodin239.client.config

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.util.concurrent.TimeUnit

@Configuration
class AppConfig {
    @Value("\${market.url}")
    private val baseUrl: String? = null

    @Bean
    fun webClientWithTimeout(): WebClient = WebClient.builder()
        .baseUrl(baseUrl!!)
        .clientConnector(ReactorClientHttpConnector(HttpClient
            .create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
            .doOnConnected { connection ->
                connection.addHandlerLast(ReadTimeoutHandler(TIMEOUT.toLong(), TimeUnit.MILLISECONDS))
                connection.addHandlerLast(WriteTimeoutHandler(TIMEOUT.toLong(), TimeUnit.MILLISECONDS))
            }
        ))
        .build()
}

const val TIMEOUT = 3500
