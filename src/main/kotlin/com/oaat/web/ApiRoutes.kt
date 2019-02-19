/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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