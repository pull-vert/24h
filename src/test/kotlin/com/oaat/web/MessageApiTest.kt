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

import com.fasterxml.jackson.databind.ObjectMapper
import com.oaat.REACTOR_IS_OUT_UUID
import com.oaat.security.JWTUtil
import com.oaat.web.dtos.MessageGetDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.expectBodyList

internal class MessageApiTest(
        @LocalServerPort port: Int,
        @Autowired jwtUtil: JWTUtil,
        @Autowired objectMapper: ObjectMapper
) : ApiTest(port, jwtUtil, objectMapper) {

    @Test
    fun `Assert findAll contains 3 Messages`() {
        client.get().uri("/api/messages/")
                .addAuthHeader()
                .exchange()
                .expectStatus().isOk
                .expectBodyList<MessageGetDto>()
                .hasSize(3)
    }

    @Test
    fun `Verify findById returns expected Message`() {
        client.get().uri("/api/messages/{id}", REACTOR_IS_OUT_UUID)
                .addAuthHeader()
                .exchange()
                .expectStatus().isOk
                .expectBody<MessageGetDto>()
                .consumeWith { exchangeResult ->
                    val message = exchangeResult.responseBody!!
                    assertThat(message.title).isEqualTo("Reactor Bismuth is out")
                    assertThat(message.content).startsWith("<p>It is my great pleasure to announce")
                    assertThat(message.author).isEqualTo("simonbasle")
                    assertThat(message.id).isEqualTo(REACTOR_IS_OUT_UUID)
                }
    }

//    @Test
//    fun `Verify post JSON API and notifications via SSE`() {
//        client.get().uri("/api/post/notifications").accept(MediaType.TEXT_EVENT_STREAM).retrieve().bodyToFlux<MessageEvent>()
//                .take(1)
//                .doOnSubscribe {
//                    client.post().uri("/api/post/").syncBody(Message("foo", "Foo", "foo", "foo", "mark", LocalDateTime.now())).exchange().subscribe()
//                }
//                .test()
//                .consumeNextWith {
//                    assertThat(it.slug).isEqualTo("foo")
//                    assertThat(it.title).isEqualTo("Foo")
//                }
//                .verifyComplete()
//    }

}
