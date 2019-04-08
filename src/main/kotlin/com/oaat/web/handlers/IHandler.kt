/*
 * Copyright 2018-2019 OAAT's author : Fred Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.web.handlers

import com.oaat.entities.Entity
import com.oaat.services.IService
import com.oaat.web.dtos.IDto
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.bodyToMono
import java.net.URI

interface IHandler<T : Entity, GET_DTO : IDto, SAVE_DTO : IDto> : Validate {

    val service: IService<T>

    val findByIdUrl
        get(): String = TODO("override_findByIdUrl_val_with_actual_Url")

    fun entityToGetDto(entity: T): GET_DTO {
        TODO("override_entityToGetDto_func_if_needed")
    }
    fun saveDtoToEntity(saveDto: SAVE_DTO): T {
        TODO("override_saveDtoToEntity_func_if_needed")
    }

    fun findById(req: ServerRequest) =
            ok().body(service.findById(req.pathVariable("id"))
                    .map(::entityToGetDto)
                    , object : ParameterizedTypeReference<GET_DTO>() {})

    fun findAll(req: ServerRequest) =
            ok().body(service.findAll()
                    .map(::entityToGetDto)
                    , object : ParameterizedTypeReference<GET_DTO>() {})

    fun deleteById(req: ServerRequest) =
            noContent().build(service.deleteById(req.pathVariable("id")))
}

// Must use inline function and reified because of bodyToMono<> not working with normal interface fun
inline fun <T : Entity, GET_DTO : IDto, reified SAVE_DTO : IDto> IHandler<T, GET_DTO, SAVE_DTO>.save(req: ServerRequest) =
        req.bodyToMono<SAVE_DTO>()
                .doOnNext(::callValidator)
                .map(::saveDtoToEntity)
                .flatMap { entity -> service.save(entity) }
                .flatMap { entity -> created(URI.create("$findByIdUrl/${entity.id}")).build() }
