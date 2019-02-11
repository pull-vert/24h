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
