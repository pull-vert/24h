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

import com.oaat.entities.Post
import com.oaat.repositories.PostEventRepository
import com.oaat.services.MarkdownConverter
import com.oaat.services.PostService
import com.oaat.web.dtos.PostGetDto
import com.oaat.web.dtos.PostSaveDto
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyToServerSentEvents
import javax.validation.Validator

@Component
class PostHandler(override val service: PostService,
                  override val validator: Validator,
                  private val postEventRepository: PostEventRepository,
                  private val markdownConverter: MarkdownConverter
) : IHandler<Post, PostGetDto, PostSaveDto> {
    override fun entityToGetDto(entity: Post) =
            PostGetDto(entity.title, markdownConverter.invoke(entity.content), entity.author, entity.createdDate!!)

    override fun saveDtoToEntity(saveDto: PostSaveDto): Post {
        TODO("not implemented")
    }

    val notifications = postEventRepository.count()
            .flatMapMany { initialPostCount ->
                postEventRepository.findWithTailableCursorBy().skip(initialPostCount) // will only emit new PostEvents
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
