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
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.bodyToServerSentEvents
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
                    entity.createdDate!!
            )

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

    val notifications = messageEventRepository.count()
            .flatMapMany { initialPostCount ->
                messageEventRepository.findWithTailableCursorBy().skip(initialPostCount) // will only emit new PostEvents
            }.share()

//    override fun findById(req: ServerRequest) =
//            ok().body(req.queryParam("converter")
//                    .map { converter ->
//                        if (converter == "markdown") {
//                            service.findById(req.pathVariable("id")).map { post ->
//                                post.copy(
//                                        headline = markdownConverter.invoke(post.headline),
//                                        content = markdownConverter.invoke(post.content))
//                            }
//                        } else {
//                            IllegalArgumentException("Only markdown converter is supported").toMono()
//                        }
//                    }.orElse(service.findById(req.pathVariable("id"))))

    fun notifications(req: ServerRequest) =
            ok().bodyToServerSentEvents(notifications)
}
