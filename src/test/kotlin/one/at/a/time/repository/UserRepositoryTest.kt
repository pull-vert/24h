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
package one.at.a.time.repository

import one.at.a.time.model.Role.ADMIN
import one.at.a.time.model.Role.USER
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.test.test
import java.time.LocalDateTime

@ExtendWith(SpringExtension::class)
@SpringBootTest
class UserRepositoryTest(@Autowired val userRepository: UserRepository) {

    @Test
    fun `Assert findByUsername returns expected min Document`() {
        userRepository.findByUsername("min")
                .test()
                .consumeNextWith {
                    assertThat(it.username).isEqualTo("min")
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
                    assertThat(it.username).isEqualTo("sdeleuze")
                    assertThat(it.createdDate).isNotNull()
                    assertThat(it.lastModifiedDate).isNotNull()
                }.verifyComplete()
    }

    @Test
    fun `Assert save modify lastModifiedDate but not createdDate`() {
        var createdDate: LocalDateTime? = null
        var lastModifiedDate: LocalDateTime? = null
        userRepository.findByUsername("sdeleuze")
                .flatMap {
                    createdDate = it.createdDate
                    lastModifiedDate = it.lastModifiedDate
                    it.roles.remove(USER)
                    userRepository.save(it)
                }
                .test()
                .consumeNextWith {
                    println("id = ${it.id}")
                    assertThat(it.roles).hasSize(1).containsExactly(ADMIN)
                    assertThat(it.createdDate).isEqualTo(createdDate)
                    assertThat(it.lastModifiedDate).isNotEqualTo(lastModifiedDate)
                }.verifyComplete()
    }
}