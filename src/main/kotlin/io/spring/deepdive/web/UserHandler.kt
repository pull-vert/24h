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
package io.spring.deepdive.web

import io.spring.deepdive.repository.UserRepository
import org.reactivestreams.Publisher
import org.springframework.stereotype.Component

import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

@Component
class UserHandler(private val repository: UserRepository) {

    fun principal(req: ServerRequest): Mono<ServerResponse> {
        val principalPublishier = req.principal().map { p -> "Hello ${p.name} !" }
        return ok().body(principalPublishier)
    }

    fun findAll(req: ServerRequest) = ok().body(repository.findAll())

    fun findByUsername(req: ServerRequest) =
            ok().body(repository.findByUsername(req.pathVariable("username")))

}
