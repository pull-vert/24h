/*
 * Copyright 2018-2019 OAAT's author : Fred Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.entities

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.Persistable
import java.time.LocalDateTime

abstract class Entity(
        @CreatedBy var createdBy: String? = null,
        @CreatedDate var createdDate: LocalDateTime? = null,
        @LastModifiedBy var lastModifiedBy: String? = null,
        @LastModifiedDate var lastModifiedDate: LocalDateTime? = null
) : Persistable<String> {
    // Persistable functions
    override fun isNew() = (null == createdDate)

    override fun toString() = "Entity{createdBy=$createdBy, createdDate=$createdDate, lastModifiedBy=$lastModifiedBy, lastModifiedDate=$lastModifiedDate}"
}
