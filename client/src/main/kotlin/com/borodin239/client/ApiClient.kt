package com.borodin239.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.http.HttpHeaders

@Component
class ApiClient(@Autowired private val webClient: WebClient) {
    fun <T> invokePostAPI(
        path: String,
        body: Any,
        type: Class<T>
    ): Mono<T> {
        return webClient.post().uri(path)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(type)
    }

    fun <T> invokeGetAPI(
        path: String,
        headers: HttpHeaders?,
        pathParams: MultiValueMap<String, Any>?,
        queryParams: MultiValueMap<String, Any>?,
        type: Class<T>
    ): Flux<T> {
        return webClient.get().uri(path)
            .retrieve()
            .bodyToFlux(type)
    }
}