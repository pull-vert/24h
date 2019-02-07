package com.oaat.services

import com.oaat.entities.Entity
import com.oaat.repositories.IRepository
import com.oaat.web.BadRequestStatusException
import com.oaat.web.NotFoundStatusException
import reactor.core.publisher.switchIfEmpty
import reactor.core.publisher.toMono
import java.util.*

interface IService<T : Entity> {

    val repository: IRepository<T>

    fun findById(id: String) =
            id.toMono()
                    .doOnNext { _id -> _id.checkValidUuid() }
                    .flatMap { _id -> repository.findById(_id) }
                    .switchIfEmpty { throw NotFoundStatusException() }

    fun findAll() = repository.findAll()

    fun save(entity: T) = repository.save(entity)

    fun deleteById(id: String) =
            id.toMono()
                    .doOnNext { _id -> _id.checkValidUuid() }
                    .flatMap { _id -> repository.deleteById(_id) }

    fun deleteAll() = repository.deleteAll()

    private fun String.checkValidUuid() =
        try {
            UUID.fromString(this)
        } catch (e: Throwable) {
            throw BadRequestStatusException(e.localizedMessage)
        }
}
