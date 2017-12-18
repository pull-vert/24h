package io.spring.deepdive.repository

import io.spring.deepdive.model.roleAdmin
import io.spring.deepdive.model.roleUser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.test.test

@ExtendWith(SpringExtension::class)
@SpringBootTest
class UserRepositoryTest(@Autowired val userRepository: UserRepository) {

    @Test
    fun `Assert findById returns expected Document`() {
        userRepository.findById("sdeleuze")
                .test()
                .consumeNextWith {
                    assertThat(it.lastname).isEqualTo("Deleuze")
                    assertThat(it.roles).hasSize(2).containsExactly(roleUser, roleAdmin)
                }.verifyComplete()
    }
}