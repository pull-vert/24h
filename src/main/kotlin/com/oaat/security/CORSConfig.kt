/*
 * Copyright 2018-2019 OAAT's author : Fred Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.security

import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
class CORSConfig : WebFluxConfigurer {

    /**
     * For dev only : avoid CORS errors for local tests
     */
    override fun addCorsMappings(registry: CorsRegistry) {
        registry
                .addMapping("/**")
                .allowedMethods("*")
//                .allowedOrigins("*") // todo check if these 2 commented lines are needed
//                .allowedHeaders("*")
    }
}
