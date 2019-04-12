/*
 * Copyright 2018-2019 OAAT's author : Fred Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.services

import com.oaat.USER_FRED_UUID
import com.oaat.repositories.UserRepository
import com.oaat.security.JWTUtil
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder
import reactor.core.publisher.Mono
import reactor.test.test

@ExtendWith(MockitoExtension::class)
class UserServiceTest {
    lateinit var service: UserService
    lateinit var repository: UserRepository

    @BeforeEach
    private fun before(@Mock repository: UserRepository, @Mock jwtUtil: JWTUtil, @Mock passwordEncoder: PasswordEncoder) {
        this.repository = repository
        this.service = UserService(repository, jwtUtil, passwordEncoder)
    }

    @Test
    fun `Verify deleteById is working`() {
        BDDMockito.given(repository.deleteById(USER_FRED_UUID))
                .willReturn(Mono.empty())

        service.deleteById(USER_FRED_UUID)
                .test()
                .verifyComplete()
    }

    // todo test all UserService functions except "save"
}