/*
 * Copyright 2018-2019 OAAT's author : Fred Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.web.dtos

import javax.validation.constraints.NotEmpty

data class AuthRequestDto(
        @field:NotEmpty
        val username: String?,

        @field:NotEmpty
        val password: String?
) : IDto

data class AuthResponseDto(val token: String) : IDto
