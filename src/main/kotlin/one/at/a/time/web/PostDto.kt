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
package one.at.a.time.web

import one.at.a.time.MarkdownConverter
import one.at.a.time.formatDate
import one.at.a.time.model.Post
import one.at.a.time.model.User
import one.at.a.time.repository.UserRepository

data class PostDto(
        val slug: String,
        val title: String,
        val headline: String,
        val content: String,
        val author: User,
        val addedAt: String)

fun Post.toDto(userRepository: UserRepository, markdownConverter: MarkdownConverter) = userRepository.findById(author).map {
    PostDto(
            slug,
            title,
            markdownConverter.invoke(headline),
            markdownConverter.invoke(content),
            it,
            addedAt.formatDate()
    )
}