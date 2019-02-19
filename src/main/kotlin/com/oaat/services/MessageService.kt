package com.oaat.services

import com.oaat.entities.Message
import com.oaat.repositories.MessageRepository
import com.oaat.web.NotFoundStatusException
import org.springframework.stereotype.Service
import reactor.core.publisher.switchIfEmpty

@Service
class MessageService(override val repository: MessageRepository) : IService<Message> {
    /**
     * Find the [Message] by slug.
     * @param slug the slug to look up
     * @return the [Message].
     */
    fun findBySlug(slug: String) = repository.findBySlug(slug)
            .switchIfEmpty { throw NotFoundStatusException("No message found for $slug slug") }
}
