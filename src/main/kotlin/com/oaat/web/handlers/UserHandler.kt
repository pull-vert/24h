/*
 * Copyright 2018-2019 OAAT's author : Frédéric Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.web.handlers

import com.oaat.entities.User
import com.oaat.services.UserService
import com.oaat.web.dtos.UserGetDto
import com.oaat.web.dtos.UserSaveDto
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import javax.validation.Validator

@Component
class UserHandler(
        override val service: UserService,
        override val validator: Validator
): IHandler<User, UserGetDto, UserSaveDto> {

    override fun entityToGetDto(entity: User) =
            UserGetDto(
                    entity.username,
                    entity.authorities,
                    entity.isEnabled,
                    entity.id
            )

    override fun saveDtoToEntity(saveDto: UserSaveDto) =
            User(
                    saveDto.username!!,
                    saveDto.password!!
            )

    override val findByIdUrl = "/api/users"

    fun findByUsername(req: ServerRequest) =
            ServerResponse.ok().body(service.findByUsername(req.pathVariable("username")))
}
