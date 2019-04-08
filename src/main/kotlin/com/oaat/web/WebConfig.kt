/*
 * Copyright 2018-2019 OAAT's author : Fred Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
@EnableWebFlux
class WebConfig (private val objectMapper: ObjectMapper) : WebFluxConfigurer {

    /**
     * Configure Jackson to Serialize and Deserialize dates with ISO-8601 format
     */
    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        objectMapper.registerModule(JavaTimeModule())
        val defaults = configurer.defaultCodecs()
        defaults.jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper))
        defaults.jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper))
    }
}
