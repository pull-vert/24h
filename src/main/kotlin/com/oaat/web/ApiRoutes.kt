/*
 * Copyright 2018-2019 OAAT's author : Frédéric Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.web

import com.oaat.web.handlers.AuthenticationHandler
import com.oaat.web.handlers.MessageHandler
import com.oaat.web.handlers.UserHandler
import com.oaat.web.handlers.save
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.*
import org.springframework.web.reactive.function.server.router

@Configuration
class ApiRoutes(private val userHandler: UserHandler,
                private val messageHandler: MessageHandler,
                private val authenticationHandler: AuthenticationHandler
) {

    @Bean
    fun appRouter() = router {
        accept(APPLICATION_JSON).nest {
            "/api".nest {
                "/users".nest {
                    GET("/{id}", userHandler::findById)
                    GET("/username/{username}", userHandler::findByUsername)
                    GET("/username/checkUsernameAvailability", userHandler::checkUsernameAvailability)
                    GET("/username/checkEmailAvailability", userHandler::checkEmailAvailability)
                    POST("/") { req -> userHandler.save(req) }
                    DELETE("/{id}", userHandler::deleteById)
                }
                "/messages".nest {
                    GET("/", messageHandler::findAll)
                    GET("/{id}", messageHandler::findById)
                    GET("/slug/{slug}", messageHandler::findBySlug)
                    POST("/", messageHandler::save)
                    DELETE("/{id}", messageHandler::deleteById)
                }
            }
            "/auth".nest {
                POST("/", authenticationHandler::auth)
            }
        }
        (GET("/api/post/notifications") and accept(TEXT_EVENT_STREAM)).invoke(messageHandler::notifications)
    }
}
