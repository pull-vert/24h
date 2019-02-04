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

import com.oaat.web.handlers.PostHandler
import com.oaat.web.handlers.UserHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.*
import org.springframework.web.reactive.function.server.router


@Configuration
class ApiRoutes(private val userHandler: UserHandler,
                private val postHandler: PostHandler) {

    @Bean
    fun appRouter() = router {
        accept(APPLICATION_JSON).nest {
            "/api/user".nest {
                GET("/principal/", userHandler::principal)
                GET("/", userHandler::findAll)
                GET("/username/{username}", userHandler::findByUsername)
            }
            "/api/post".nest {
                GET("/", postHandler::findAll)
                GET("/{id}", postHandler::findOneById)
                POST("/", { req -> postHandler.save(req) })
                DELETE("/{id}", postHandler::delete)
            }
        }
        (GET("/api/post/notifications") and accept(TEXT_EVENT_STREAM)).invoke(postHandler::notifications)
    }

}