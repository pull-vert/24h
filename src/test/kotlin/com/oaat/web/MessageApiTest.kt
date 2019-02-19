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

import com.oaat.REACTOR_IS_OUT_UUID
import com.oaat.repositories.MessageRepository
import com.oaat.web.dtos.MessageGetDto
import com.oaat.web.dtos.MessageSaveDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.expectBodyList
import reactor.test.test

internal class MessageApiTest(
        @Autowired private val messageRepository: MessageRepository
) : ApiTest() {

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

    @Test
    fun `Verify findById with no JWT Token fails`() {
        client.get().uri("/api/messages/{id}", REACTOR_IS_OUT_UUID)
                .exchange()
                .expectStatus().isUnauthorized
    }

    @Test
    fun `Verify findById with invalid uuid uri param fails`() {
        val invalidUuid = "invalid_uuid"
        client.get().uri("/api/messages/{id}", invalidUuid)
                .addAuthHeader()
                .exchange()
                .expectStatus().isBadRequest
    }

    @Test
    fun `Verify save Message ok`() {
        client.post().uri("/api/messages/")
                .syncBody(MessageSaveDto("my test message", "my test message content"))
                .addAuthHeader()
                .exchange()
                .expectStatus().isCreated
                .expectHeader().value("location") { uri ->
                    assertThat(uri).startsWith("/api/messages/")
                    // Then call the returned uri and verify the it returns saved User resource
                    client.get().uri(uri)
                            .addAuthHeader()
                            .exchange()
                            .expectStatus().isOk
                            .expectBody<MessageGetDto>()
                            .consumeWith { exchangeResult ->
                                val message = exchangeResult.responseBody!!
                                assertThat(message.title).isEqualTo("my test message")
                                assertThat(message.content).startsWith("<p>my test message content")
                                assertThat(message.author).isEqualTo("Fred")
                                assertThat(message.id).isNotEmpty()
                                // then delete the Message we just saved to ensure immutable MongoDB state
                                messageRepository.deleteById(message.id)
                                        .test()
                                        .verifyComplete()
                            }

                }
    }

    @Test
    fun `Verify save Message with title too long bean validation fails`() {
        client.post().uri("/api/messages/")
                .syncBody(MessageSaveDto("too long title too long title too long title too long title too long title too long title too long title" +
                        "too long title too long title too long title too long title too long title too long title too long title" +
                        "too long title too long title too long title too long title too long title too long title too long title",
                        "my test message content"))
                .addAuthHeader()
                .exchange()
                .expectStatus().isBadRequest
                .expectBody<ServerResponseError>()
                .consumeWith { exchangeResult ->
                    val error = exchangeResult.responseBody!!
                    assertThat(error["message"] as String).contains("title(too long title too", "2", "128")
                    assertThat(error["path"]).isEqualTo("/api/messages/")
                    assertThat(error["timestamp"]).isNotNull
                    assertThat(error["status"]).isEqualTo(400)
                    assertThat(error["error"]).isEqualTo("Bad Request")
                }
    }

    @Test
    fun `Verify save Message with no title bean validation fails`() {
        client.post().uri("/api/messages/")
                .syncBody(MessageSaveDto(null, "my test message content"))
                .addAuthHeader()
                .exchange()
                .expectStatus().isBadRequest
                .expectBody<ServerResponseError>()
                .consumeWith { exchangeResult ->
                    val error = exchangeResult.responseBody!!
                    assertThat(error["message"] as String).contains("title(null)")
                    assertThat(error["path"]).isEqualTo("/api/messages/")
                    assertThat(error["timestamp"]).isNotNull
                    assertThat(error["status"]).isEqualTo(400)
                    assertThat(error["error"]).isEqualTo("Bad Request")
                }
    }

    @Test
    fun `Verify findBySlug returns expected Message`() {
        client.get().uri("/api/messages/slug/{slug}", "reactor-bismuth-is-out")
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

    @Test
    fun `Verify findBySlug returns 404 Not Found if provided slug is not found`() {
        val slug = "no-slug"
        client.get().uri("/api/messages/slug/{slug}", slug)
                .addAuthHeader()
                .exchange()
                .expectStatus().isNotFound
                .expectBody<ServerResponseError>()
                .consumeWith { exchangeResult ->
                    val error = exchangeResult.responseBody!!
                    assertThat(error["message"] as String).isEqualTo("No message found for $slug slug")
                    assertThat(error["path"]).isEqualTo("/api/messages/slug/no-slug")
                    assertThat(error["timestamp"]).isNotNull
                    assertThat(error["status"]).isEqualTo(404)
                    assertThat(error["error"]).isEqualTo("Not Found")
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

    @Test
    fun `Message findAll doc`() {
        client.get().uri("/api/messages/")
                .addAuthHeader()
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .consumeWith(document("findAllMessages",
                        responseFields(
                                fieldWithPath("[]").description("An array of Messages"))
                                .andWithPrefix("[].", *messageFields())))
    }

    @Test
    fun `Message findById doc`() {
        client.get().uri("/api/messages/{id}", REACTOR_IS_OUT_UUID)
                .addAuthHeader()
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .consumeWith(document("findByIdMessage",
                        pathParameters(parameterWithName("id").description("ID of the Message to search for")),
                        responseFields(*messageFields())))
    }

    @Test
    fun `Message Save doc`() {
        client.post().uri("/api/messages/")
                .syncBody(MessageSaveDto("my test message", "my test message content"))
                .addAuthHeader()
                .exchange()
                .expectStatus().isCreated
                .expectBody()
                .consumeWith(document("saveMessage",
                        requestFields(
                                fieldWithPath("title").description("Title of the Message"),
                                fieldWithPath("content").description("Content of the Message in Markdown format")
                        ),
                        responseHeaders(
                                headerWithName("Location").description("GET URI for accessing created Message by its ID")
                        )))
        // then delete the Message we just saved to ensure immutable MongoDB state
        messageRepository.findBySlug("my-test-message")
                .flatMap { message -> messageRepository.deleteById(message.id) }
                .test()
                .verifyComplete()
    }

    @Test
    fun `Message findBySlug doc`() {
        client.get().uri("/api/messages/slug/{slug}", "reactor-bismuth-is-out")
                .addAuthHeader()
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .consumeWith(document("findBySlugMessage",
                        pathParameters(parameterWithName("slug").description("Slug of the Message to search for")),
                        responseFields(*messageFields())))
    }

    /**
     * Message fields used in responses.
     *
     * @return
     */
    private fun messageFields() = arrayOf(
            fieldWithPath("title").description("Title of the message"),
            fieldWithPath("slug").description("Slug created from Title"),
            fieldWithPath("content").description("Content of the Message in HTML format"),
            fieldWithPath("author").description("Author of the Message, corresponds to authenticated user that POSTed the Message"),
            fieldWithPath("id").description("ID of the Message document"),
            fieldWithPath("addedAt").description("Date of Message creation")
    )
}
