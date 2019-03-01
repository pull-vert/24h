/*
 * Copyright 2018-2019 OAAT's author : Frédéric Montariol. Use of this source code is governed by the Apache 2.0 license.
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
class MessageHandler(
        override val service: MessageService,
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
