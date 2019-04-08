/*
 * Copyright 2018-2019 OAAT's author : Fred Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Message(
    val title: String,
    val slug: String,
//    val headline: String,
    val content: String,
    val author: String,
    private val id: String = UUID.randomUUID().toString()
) : Entity() {

    // Persistable function
    @Id
    override fun getId() = id
}
