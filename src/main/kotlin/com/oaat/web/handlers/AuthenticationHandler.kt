/*
 * Copyright 2018-2019 OAAT's author : Fred Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.web.handlers

import com.oaat.services.UserService
import com.oaat.web.dtos.AuthRequestDto
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import javax.validation.Validator

@Component
class AuthenticationHandler(
        private val userService: UserService,
        override val validator: Validator
) : Validate {

    fun auth(req: ServerRequest) =
            req.bodyToMono<AuthRequestDto>()
                    .doOnNext(::callValidator)
                    .flatMap { authRequest -> ok().body(userService.auth(authRequest)) }
}
