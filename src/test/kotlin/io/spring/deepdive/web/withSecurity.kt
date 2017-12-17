package io.spring.deepdive.web

import org.springframework.web.reactive.function.client.WebClient
import java.util.*

private fun buildAutorizationHeader(): String {
    val auth = "sdeleuze:password"
    val encodedAuth = Base64.getEncoder().encode(
            auth.toByteArray(Charsets.UTF_8))
    return "Basic " + String(encodedAuth)
}

fun WebClient.RequestHeadersSpec<*>.addAuthHeader() =
        this.header("Authorization", buildAutorizationHeader())