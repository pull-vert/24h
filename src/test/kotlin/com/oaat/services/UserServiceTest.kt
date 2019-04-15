/*
 * Copyright 2018-2019 OAAT's author : Fred Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.services

import com.oaat.USER_FRED_UUID
import com.oaat.entities.User
import com.oaat.repositories.UserRepository
import com.oaat.security.JWTUtil
import com.oaat.web.dtos.AuthRequestDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import reactor.test.test

@ExtendWith(MockitoExtension::class)
class UserServiceTest {
    private lateinit var service: UserService
    private lateinit var repository: UserRepository
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var jwtUtil: JWTUtil

    @BeforeEach
    private fun before(@Mock repository: UserRepository, @Mock jwtUtil: JWTUtil, @Mock passwordEncoder: PasswordEncoder) {
        this.repository = repository
        this.passwordEncoder = passwordEncoder
        this.jwtUtil = jwtUtil
        this.service = UserService(repository, jwtUtil, passwordEncoder)
    }

    @Test
    fun `Verify deleteById is working`() {
        given(repository.deleteById(USER_FRED_UUID))
                .willReturn(Mono.empty())

        service.deleteById(USER_FRED_UUID)
                .test()
                .verifyComplete()
    }

    @Test
    fun `Verify auth nominal case`() {
        val username = "user"
        val encodedPassword = "encodedPassword"
        val rawPassword = "rawPassword"
        val user = User("user", encodedPassword, "whatever")
        val token = "token"
        given(repository.findByUsername(username))
                .willReturn(user.toMono())
        given(passwordEncoder.matches(rawPassword, encodedPassword))
                .willReturn(true)
        given(jwtUtil.generateToken(user))
                .willReturn(token)

        service.auth(AuthRequestDto("user", rawPassword))
                .test()
                .assertNext { response ->
                    assertThat(response.token).isEqualTo(token)
                }
                .verifyComplete()
    }

    // todo test all UserService functions except "save"
}