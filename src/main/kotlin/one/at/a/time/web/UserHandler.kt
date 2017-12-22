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
package one.at.a.time.web

import one.at.a.time.repository.UserRepository
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

@Component
class UserHandler(private val repository: UserRepository) {

    fun principal(req: ServerRequest): Mono<ServerResponse> {
        val principalPublishier = req.principal().map { "Hello ${it.name} !" }
        return ok().body(principalPublishier)
    }

    fun findAll(req: ServerRequest) = ok().body(repository.findAll())

    fun findByUsername(req: ServerRequest) =
            ok().body(repository.findByUsername(req.pathVariable("username")))

}
