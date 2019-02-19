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
package com.oaat.web.handlers

import com.oaat.entities.Message
import com.oaat.repositories.MessageEventRepository
import com.oaat.services.MarkdownConverter
import com.oaat.services.MessageService
import com.oaat.slugify
import com.oaat.web.dtos.MessageGetDto
import com.oaat.web.dtos.MessageSaveDto
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import java.net.URI
import javax.validation.Validator

@Component
class MessageHandler(override val service: MessageService,
                     override val validator: Validator,
                     private val messageEventRepository: MessageEventRepository,
                     private val markdownConverter: MarkdownConverter
) : IHandler<Message, MessageGetDto, MessageSaveDto> {
    override fun entityToGetDto(entity: Message) =
            MessageGetDto(
                    entity.title,
                    entity.slug,
                    markdownConverter.invoke(entity.content),
                    entity.author,
                    entity.id,
                    entity.createdDate!!
            )

    override val findByIdUrl = "/api/messages"

    private fun saveDtoToEntity(saveDto: MessageSaveDto, principalName: String) =
            Message(
                    saveDto.title!!,
                    saveDto.title.slugify(),
                    saveDto.content!!,
                    principalName
            )

    fun save(req: ServerRequest) =
            req.bodyToMono<MessageSaveDto>()
                    .doOnNext(::callValidator)
                    .zipWith(req.principal()) { saveDto, principal -> saveDtoToEntity(saveDto, principal.name) }
                    .flatMap { entity -> service.save(entity) }
                    .flatMap { entity -> ServerResponse.created(URI.create("$findByIdUrl/${entity.id}")).build() }

    fun findBySlug(req: ServerRequest) =
            ServerResponse.ok().body(
                    service.findBySlug(req.pathVariable("slug"))
                            .map(::entityToGetDto)
            )

    val notifications = messageEventRepository.count()
            .flatMapMany { initialPostCount ->
                messageEventRepository.findWithTailableCursorBy().skip(initialPostCount) // will only emit new PostEvents
            }.share()

    fun notifications(req: ServerRequest) =
            ok().bodyToServerSentEvents(notifications)
}
