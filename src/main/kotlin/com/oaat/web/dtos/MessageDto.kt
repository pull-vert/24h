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
package com.oaat.web.dtos

import java.time.LocalDateTime
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class MessageSaveDto(
        @field:NotEmpty
        val title: String?,

        @field:NotEmpty
        @field:Size(max = 1024)
        val content: String?
) : IDto

data class MessageGetDto(
        val title: String,
        val slug: String,
//        val headline: String,
        val content: String,
        val author: String,
        val addedAt: LocalDateTime
) : IDto
