/*
 * Copyright 2018-2019 OAAT's author : Frédéric Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.web.dtos

import java.time.LocalDateTime
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class MessageSaveDto(
        @field:NotEmpty
        @field:Size(min = 2, max = 128)
        val title: String?,

        @field:NotEmpty
        @field:Size(min = 2, max = 1024)
        val content: String?
) : IDto

data class MessageGetDto(
        val title: String,
        val slug: String,
//        val headline: String,
        val content: String,
        val author: String,
        val id: String,
        val addedAt: LocalDateTime
) : IDto
