/*
 * Copyright 2018-2019 OAAT's author : Frédéric Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.repositories

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.test.test

@ExtendWith(SpringExtension::class)
@SpringBootTest
class MessageRepositoryTest(@Autowired private val messageRepository: MessageRepository) {

    @Test
    fun `Verify findBySlug returns existing reactor-bismuth-is-out Message`() {
        messageRepository.findBySlug("reactor-bismuth-is-out")
                .test()
                .consumeNextWith { message ->
                    assertThat(message.title).isEqualTo("Reactor Bismuth is out")
                    assertThat(message.slug).isEqualTo("reactor-bismuth-is-out")
                    assertThat(message.id).isNotNull()
                }.verifyComplete()
    }

    @Test
    fun `Verify findBySlug returns no no-slug Message`() {
        messageRepository.findBySlug("no-slug")
                .test()
                .verifyComplete()
    }

    @Test
    fun `Verify findAll returns 3 Messages`() {
        messageRepository.findAll()
                .test()
                .expectNextCount(3)
                .verifyComplete()
    }
}
