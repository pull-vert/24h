/*
 * Copyright 2018-2019 OAAT's author : Frédéric Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class MessageEvent(
        val title: String,
        private val id: String = UUID.randomUUID().toString()
) : Entity() {

    // Persistable function
    @Id
    override fun getId() = id
}