/*
 * Copyright 2018-2019 OAAT's author : Frédéric Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.web.dtos

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.oaat.USER_FRED_UUID
import com.oaat.entities.Role.ROLE_USER
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.util.ResourceUtils

@ExtendWith(SpringExtension::class)
@SpringBootTest
class JacksonTest(@Autowired private val objectMapper: ObjectMapper) {

    @Test
    fun `Verify User serialize works`() {
        val user = UserGetDto("Fred", "fred@mail.com", listOf(ROLE_USER), true, USER_FRED_UUID)
        val json = objectMapper.writeValueAsString(user)
        assertThat(json).isEqualTo("{\"username\":\"Fred\",\"email\":\"fred@mail.com\"," +
                "\"authorities\":[\"ROLE_USER\"],\"enabled\":true,\"id\":\"79e9eb45-2835-49c8-ad3b-c951b591bc7f\"}")
    }

    @Test
    fun `Verify User deserialize all fields works`() {
        val file = ResourceUtils.getFile("classpath:web/dtos/user.json")
        val user = objectMapper.readValue<UserSaveDto>(file)
        assertThat(user.username).isEqualTo("Fred")
        assertThat(user.password).isEqualTo("password")
    }

    @Test
    fun `Verify User deserialize no password works`() {
        val file = ResourceUtils.getFile("classpath:web/dtos/user_no_password.json")
        val user = objectMapper.readValue<UserSaveDto>(file)
        assertThat(user.username).isEqualTo("Fred")
        assertThat(user.password).isNull()
    }
}
