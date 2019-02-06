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
            repository.findById(id.toUuid())
                    .switchIfEmpty { throw NotFoundStatusException() }

    fun findAll() = repository.findAll()

    fun save(entity: T) = repository.save(entity)

    fun deleteById(id: String) = repository.deleteById(id.toUuid())

    fun deleteAll() = repository.deleteAll()

    private fun String.toUuid() =
            try {
                UUID.fromString(this)
            } catch(e: Throwable) {
                throw BadRequestStatusException(e.localizedMessage)
            }
}
