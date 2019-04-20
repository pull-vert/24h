/*
 * Copyright 2018-2019 OAAT's author : Fred Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.services

import com.oaat.entities.Message
import com.oaat.repositories.MessageRepository
import com.oaat.web.NotFoundStatusException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import reactor.test.test

@ExtendWith(MockitoExtension::class)
class MessageServiceTest {
    private lateinit var service: MessageService
    private lateinit var repository: MessageRepository

    @BeforeEach
    private fun before(@Mock repository: MessageRepository) {
        this.repository = repository
        this.service = MessageService(repository)
    }

    @Test
    fun `Verify findBySlug return one value`() {
        val msg = Message("Title", "title", "content", "Fred")
        val slug = "title"
        given(repository.findBySlug(slug))
                .willReturn(msg.toMono())

        service.findBySlug(slug)
                .test()
                .assertNext { message ->
                    assertThat(message).isEqualTo(msg)
                }.verifyComplete()
    }

    @Test
    fun `Verify findBySlug no value throws NotFoundStatusException`() {
        val slug = "title"
        given(repository.findBySlug(slug))
                .willReturn(Mono.empty())

        service.findBySlug(slug)
                .test()
                .consumeErrorWith { thrown ->
                    assertThat(thrown).isInstanceOf(NotFoundStatusException::class.java)
                            .hasMessageContaining("404 NOT_FOUND")
                            .hasMessageContaining("No Message found for $slug slug")
                }.verify()
    }
}
