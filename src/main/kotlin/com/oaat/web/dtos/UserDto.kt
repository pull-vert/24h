/*
 * Copyright 2018-2019 OAAT's author : Frédéric Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.web.dtos

import com.oaat.entities.Role
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class UserSaveDto(
        @field:NotEmpty
        @field:Size(min = 2, max = 200)
        val username: String?,

        @field:NotEmpty
        @field:Size(min = 8, max = 200)
        val password: String?,

        @field:NotEmpty
        @field:Size(min = 8, max = 200)
        @field:Email
        val email: String?
) : IDto

data class UserGetDto(
        val username: String,
        val email: String,
        val authorities: List<Role>,
        val enabled: Boolean,
        val id: String
) : IDto
