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
    lateinit var messageService: MessageService
    lateinit var repository: MessageRepository

    @BeforeEach
    private fun before(@Mock repository: MessageRepository) {
        this.repository = repository
        this.messageService = MessageService(repository)
    }

    @Test
    fun `Verify findBySlug return one value`() {
        val msg = Message("Title", "title", "content", "Fred")
        val slug = "title"
        given(repository.findBySlug(slug))
                .willReturn(msg.toMono())

        messageService.findBySlug(slug)
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

        messageService.findBySlug(slug)
                .test()
                .consumeErrorWith { thrown ->
                    assertThat(thrown).isInstanceOf(NotFoundStatusException::class.java)
                            .hasMessageContaining("404 NOT_FOUND")
                            .hasMessageContaining("No message found for $slug slug")
                }.verify()
    }
}
