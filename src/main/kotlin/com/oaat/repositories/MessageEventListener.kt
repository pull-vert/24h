/*
 * Copyright 2018-2019 OAAT's author : Fred Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.repositories

import com.oaat.entities.Message
import com.oaat.entities.MessageEvent
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent
import org.springframework.stereotype.Component

//@Component
//class MessageEventListener(private val postEventRepository: MessageEventRepository) : AbstractMongoEventListener<Message>() {
//
//    override fun onAfterSave(event: AfterSaveEvent<Message>) {
//        postEventRepository.save(MessageEvent(event.source.slug, event.source.title)).block()
//    }
//
//}
