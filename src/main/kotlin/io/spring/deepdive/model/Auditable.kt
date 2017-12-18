package io.spring.deepdive.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.Persistable
import java.time.LocalDateTime
import java.util.*

abstract class Auditable(
        @CreatedDate var createdDate: LocalDateTime? = null,
        @LastModifiedDate var lastModifiedDate: LocalDateTime? = null,
        @Id private val id: Any = UUID.randomUUID()): Persistable<Any> {
    // Persistable functions
    override fun isNew() = (null == createdDate)
    override fun getId() = id
}