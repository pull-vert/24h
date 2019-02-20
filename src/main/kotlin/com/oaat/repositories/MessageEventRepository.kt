/*
 * Copyright 2018-2019 OAAT's author : Frédéric Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.repositories

import com.oaat.entities.MessageEvent
import org.springframework.data.mongodb.repository.Tailable
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface MessageEventRepository : IRepository<MessageEvent> {

    @Tailable
    fun findWithTailableCursorBy(): Flux<MessageEvent>
}
