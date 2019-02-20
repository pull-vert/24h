/*
 * Copyright 2018-2019 OAAT's author : Frédéric Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.repositories

import com.oaat.entities.Message
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface MessageRepository : IRepository<Message> {
    /**
     * Find the [Message] by slug.
     * @param slug the slug to look up
     * @return the [Message].
     */
    fun findBySlug(slug: String): Mono<Message>
}
