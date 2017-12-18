/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.spring.deepdive.repository

import io.spring.deepdive.model.Role.ADMIN
import io.spring.deepdive.model.Role.USER
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.test.test
import java.time.Duration

@ExtendWith(SpringExtension::class)
@SpringBootTest
class UserRepositoryTest(@Autowired val userRepository: UserRepository) {

    @Test
    fun `Assert findByUsername returns expected min Document`() {
        userRepository.findByUsername("min")
                .test()
                .consumeNextWith {
                    assertThat(it.lastname).isNull()
                    assertThat(it.name).isEqualTo("min")
                    assertThat(it.roles).hasSize(1).containsExactly(USER)
                    assertThat(it.createdDate).isNotNull()
                    assertThat(it.lastModifiedDate).isNotNull()
                }.verifyComplete()
    }

    @Test
    fun `Assert findByUsername returns expected Document`() {
        userRepository.findByUsername("sdeleuze")
                .test()
                .consumeNextWith {
                    assertThat(it.lastname).isEqualTo("Deleuze")
                    assertThat(it.name).isEqualTo("sdeleuze : Sebastien Deleuze")
                    assertThat(it.roles).hasSize(2).containsExactly(USER, ADMIN)
                    assertThat(it.createdDate).isNotNull()
                    assertThat(it.lastModifiedDate).isNotNull()
                }.verifyComplete()
    }

    @Test
    fun `Assert save modify lastModifiedDate but not createdDate`() {
        userRepository.findByUsername("sdeleuze")
                .test()
                .consumeNextWith {
                    val createdDate = it.createdDate
                    val lastModifiedDate = it.lastModifiedDate
                    it.email = "sebdeleuze@pivotal.com"
                    userRepository.save(it)
                            .delayElement(Duration.ofMillis(2))
                            .test()
                            .consumeNextWith {
                                println("id = ${it.id}")
                                assertThat(it.email).isEqualTo("sebdeleuze@pivotal.com")
                                assertThat(it.createdDate).isEqualTo(createdDate)
                                assertThat(it.lastModifiedDate).isNotEqualTo(lastModifiedDate)
                            }.verifyComplete()
                }.verifyComplete()
    }
}