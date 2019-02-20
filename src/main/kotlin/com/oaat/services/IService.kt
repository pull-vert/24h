/*
 * Copyright 2018-2019 OAAT's author : Frédéric Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.services

import com.oaat.entities.Entity
import com.oaat.repositories.IRepository
import com.oaat.web.BadRequestStatusException
import com.oaat.web.NotFoundStatusException
import reactor.core.publisher.switchIfEmpty
import java.util.*

interface IService<T : Entity> {

    val repository: IRepository<T>

    fun findById(id: String) =
            repository.findById(id.checkValidUuid())
                    .switchIfEmpty { throw NotFoundStatusException() }

    fun findAll() = repository.findAll()

    fun save(entity: T) = repository.save(entity)

    fun deleteById(id: String) = repository.deleteById(id.checkValidUuid())

    fun deleteAll() = repository.deleteAll()

    private fun String.checkValidUuid() =
            try {
                UUID.fromString(this)
                this
            } catch (e: Throwable) {
                throw BadRequestStatusException(e.localizedMessage)
            }
}
